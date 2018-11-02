package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.TypeAdaptorGenerator;

import javax.lang.model.element.Modifier;
import java.util.Map;

public class ResolverConstructorGenerator {

    private ControllerGenerator controllerGenerator;
    private Map<TypeName, TypeAdaptorGenerator> typeAdaptors;
    private LighterTypes types;

    public ResolverConstructorGenerator
            (ControllerGenerator controllerGenerator, Map<TypeName, TypeAdaptorGenerator> typeAdaptors, LighterTypes types) {
        this.controllerGenerator = controllerGenerator;
        this.typeAdaptors = typeAdaptors;
        this.types = types;
    }

    public MethodSpec make() {
        TypeName controllerType = TypeName.get(controllerGenerator.getType());
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeAdapterFactory.class, "adaptorFactory", Modifier.FINAL)
                .addParameter(controllerType, "controller", Modifier.FINAL)
                .addCode(makeCode())
                .build();
    }

    private CodeBlock makeCode() {
        return null;
    }
}
