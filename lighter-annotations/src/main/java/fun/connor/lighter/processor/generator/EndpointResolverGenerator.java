package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.MoreTypes;
import fun.connor.lighter.processor.generator.codegen.TypeAdaptorGenerator;
import fun.connor.lighter.processor.generator.endpoint.ControllerGenerator;
import fun.connor.lighter.processor.generator.endpoint.ResolveMethodGenerator;
import fun.connor.lighter.processor.generator.endpoint.ResolverConstructorGenerator;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EndpointResolverGenerator extends AbstractGenerator {

    private Endpoint endpoint;
    private Controller controller;
    private LighterTypes types;

    public EndpointResolverGenerator(Controller controller, Endpoint endpoint, LighterTypes types, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;
        this.types = types;
    }

    @Override
    protected TypeSpec generateType() {
        Map<TypeName, TypeAdaptorGenerator> typeAdaptorGenerators = getAllRequiredTypes().stream()
                .map(t -> new TypeAdaptorGenerator(t, types))
                .collect(Collectors.toMap(a -> TypeName.get(a.getAdaptingType()), Function.identity()));

        TypeSpec.Builder builder = TypeSpec.classBuilder(makeClassName())
                .addSuperinterface(LighterRequestResolver.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        typeAdaptorGenerators.values().stream()
                .map(TypeAdaptorGenerator::getField)
                .forEach(builder::addField);

        ControllerGenerator controllerGenerator =
                new ControllerGenerator
                        (controller.getElement().asType(), "controller",
                                endpoint.getMethodName(), endpoint.getReturnType());

        builder.addField(controllerGenerator.getField());

        builder.addMethod(new ResolverConstructorGenerator(controllerGenerator, typeAdaptorGenerators, types).make());
        builder.addMethod(new ResolveMethodGenerator(typeAdaptorGenerators, types, controllerGenerator, endpoint).make());


        return builder.build();
    }



    private List<TypeMirror> getAllRequiredTypes() {
        List<TypeMirror> requiredTypes = new ArrayList<>();

        List<TypeMirror> methodArguments = endpoint.getMethodArgumentTypes();
        TypeMirror returnType = endpoint.getReturnTypeParameter();

        requiredTypes.add(returnType);
        for (TypeMirror mirror : methodArguments) {
            if (MoreTypes.isTypeOptional(mirror)) {
                mirror = types.extractOptionalType((DeclaredType) mirror);
            }
            TypeMirror erased = types.erasure(mirror);
            if (!types.collectionContains(requiredTypes, erased)) {
                requiredTypes.add(erased);
            }
        }
        return requiredTypes;
    }

    private String makeClassName() {
        String methodName = endpoint.getMethodName();

        String randomStr = UUID.nameUUIDFromBytes((endpoint.getHttpMethod().toString() + endpoint.pathTemplate())
                .getBytes()).toString().replace("-", "");

        return methodName + randomStr;
    }

    @Override
    protected String getGeneratedPackageName() {
        return GENERATED_PACKAGE_NAME + "." + controller.getContainingName();
    }
}
