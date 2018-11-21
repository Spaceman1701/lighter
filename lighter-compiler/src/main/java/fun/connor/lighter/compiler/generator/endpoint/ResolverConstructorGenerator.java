package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.Assignable;
import fun.connor.lighter.compiler.generator.codegen.Assignment;
import fun.connor.lighter.compiler.generator.codegen.Expression;
import fun.connor.lighter.compiler.generator.codegen.Statement;
import fun.connor.lighter.injection.InjectionObjectFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

public class ResolverConstructorGenerator {

    private ControllerGenerator controllerGenerator;
    private TypeAdaptorFactoryGenerator factoryGenerator;
    private Map<TypeName, RequestGuardFactoryGenerator> requestGuardFactories;
    private LighterTypes types;

    public ResolverConstructorGenerator
            (ControllerGenerator controllerGenerator,
             TypeAdaptorFactoryGenerator typeAdaptorFactoryGenerator,
             Map<TypeName, RequestGuardFactoryGenerator> requestGuardFactories, LighterTypes types) {
        this.controllerGenerator = controllerGenerator;
        this.factoryGenerator = typeAdaptorFactoryGenerator;
        this.requestGuardFactories = requestGuardFactories;
        this.types = types;
    }

    public MethodSpec make() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(InjectionObjectFactory.class, "injector", Modifier.FINAL)
                .addParameter(TypeAdapterFactory.class, "adaptorFactory", Modifier.FINAL)
                .addCode(makeCode())
                .build();
    }


    private CodeBlock makeCode() {
        CodeBlock.Builder builder = CodeBlock.builder();

        builder.addStatement(makeControllerAssignment().make());

        builder.addStatement(Assignment.of(factoryGenerator.makeAssignable(),
                Expression.code(factoryGenerator.getType(), "adaptorFactory")).make());

        builder.add(makeRequestGuardFactories());

        return builder.build();
    }

    private CodeBlock makeRequestGuardFactories() {
        CodeBlock.Builder builder = CodeBlock.builder();
        InjectorGenerator injectorGenerator =
                new InjectorGenerator
                        (Expression.code(types.mirrorOfClass(InjectionObjectFactory.class), "injector"));
        for (Map.Entry<TypeName, RequestGuardFactoryGenerator> entry : requestGuardFactories.entrySet()) {
            RequestGuardFactoryGenerator generator = entry.getValue();
            builder.addStatement(Assignment.of(generator.getAssignable(),
                    injectorGenerator.newInstance(generator.getType())).make());
        }

        return builder.build();
    }

    private Statement makeControllerAssignment() {
        Assignable controllerAssign = controllerGenerator.makeAssignable();
        Expression controllerExpr = new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("injector.newInstance($T.class)", controllerAssign.getType());
            }

            @Override
            public TypeMirror getType() {
                return controllerAssign.getType();
            }
        };
        return Assignment.of(controllerAssign, controllerExpr);
    }
}
