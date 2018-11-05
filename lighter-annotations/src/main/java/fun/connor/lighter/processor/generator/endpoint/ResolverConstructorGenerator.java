package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.injection.InjectionObjectFactory;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.*;
import fun.connor.lighter.processor.model.RequestGuardFactory;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

public class ResolverConstructorGenerator {

    private ControllerGenerator controllerGenerator;
    private Map<TypeName, TypeAdaptorGenerator> typeAdaptors;
    private TypeAdaptorFactoryGenerator factoryGenerator;
    private Map<TypeName, RequestGuardFactoryGenerator> requestGuardFactories;
    private LighterTypes types;

    public ResolverConstructorGenerator
            (ControllerGenerator controllerGenerator, Map<TypeName,
             TypeAdaptorGenerator> typeAdaptors,
             TypeAdaptorFactoryGenerator typeAdaptorFactoryGenerator,
             Map<TypeName, RequestGuardFactoryGenerator> requestGuardFactories, LighterTypes types) {
        this.controllerGenerator = controllerGenerator;
        this.typeAdaptors = typeAdaptors;
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
        for (TypeAdaptorGenerator generator : typeAdaptors.values()) {
            builder.addStatement(makeTypeAdaptorAssignment(generator).make());
        }

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

    private Statement makeTypeAdaptorAssignment(TypeAdaptorGenerator generator) {
        Assignable field = generator.makeAssignable();
        Expression typeAdaptorExpr = new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.getAdapter($T.class)", "adaptorFactory", generator.getAdaptingType());
            }

            @Override
            public TypeMirror getType() {
                return field.getType();
            }
        };
        return Assignment.of(field, typeAdaptorExpr);
    }
}
