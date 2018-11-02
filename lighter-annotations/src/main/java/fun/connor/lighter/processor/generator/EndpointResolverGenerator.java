package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.TypeAdaptorGenerator;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;

import javax.annotation.processing.Filer;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EndpointResolverGenerator extends AbstractGenerator {

    private Endpoint endpoint;
    private Controller controller;
    private LighterTypes types;

    private Map<TypeName, TypeAdaptorGenerator>  typeAdaptorGenerators;

    public EndpointResolverGenerator(Controller controller, Endpoint endpoint, LighterTypes types, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;
        this.types = types;
    }

    @Override
    protected TypeSpec generateType() {
        typeAdaptorGenerators = getAllRequiredTypes().stream()
                .map(t -> new TypeAdaptorGenerator(t, types))
                .collect(Collectors.toMap(a -> TypeName.get(a.getAdaptingType()), Function.identity()));

        TypeSpec.Builder builder = TypeSpec.classBuilder(makeClassName());

        typeAdaptorGenerators.values().stream()
                .map(TypeAdaptorGenerator::getField)
                .forEach(builder::addField);

        return null;
    }

    private List<TypeMirror> getAllRequiredTypes() {
        List<TypeMirror> requiredTypes = new ArrayList<>();

        List<TypeMirror> methodArguments = endpoint.getMethodArgumentTypes();
        TypeMirror returnType = endpoint.getReturnTypeParameter();

        requiredTypes.add(returnType);
        for (TypeMirror mirror : methodArguments) {
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
