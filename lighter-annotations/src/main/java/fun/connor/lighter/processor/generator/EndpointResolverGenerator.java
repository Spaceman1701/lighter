package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.MoreTypes;
import fun.connor.lighter.processor.generator.endpoint.*;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.RequestGuards;
import fun.connor.lighter.processor.model.endpoint.MethodParameter;

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
    private RequestGuards requestGuards;
    private LighterTypes types;

    public EndpointResolverGenerator(Controller controller, Endpoint endpoint,
                                     RequestGuards requestGuards, LighterTypes types, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;
        this.requestGuards = requestGuards;
        this.types = types;
    }

    @Override
    protected TypeSpec generateType() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(makeClassName())
                .addSuperinterface(LighterRequestResolver.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        Map<MethodParameter.Source, List<MethodParameter>> params =
                endpoint.getParametersBySource();

        List<TypeMirror> parametersForAdapting = params.entrySet().stream()
                .filter(e -> requiresAdaptor(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .map(MethodParameter::getType)
                .collect(Collectors.toList());
        Map<TypeName, TypeAdaptorGenerator> typeAdaptorGenerators = makeTypeAdaptors(parametersForAdapting);

        typeAdaptorGenerators.values().stream()
                .map(TypeAdaptorGenerator::getField)
                .forEach(builder::addField);

        TypeAdaptorFactoryGenerator typeAdaptorFactoryGenerator
                = new TypeAdaptorFactoryGenerator("typeAdaptorFactory", types);
        builder.addField(typeAdaptorFactoryGenerator.getField());

        List<TypeMirror> typesForGuards = params.get(MethodParameter.Source.GUARD).stream()
                .map(MethodParameter::getType)
                .collect(Collectors.toList());
        Map<TypeName, RequestGuardFactoryGenerator> requestGuardFactories = makeRequestGuardFactories(typesForGuards);

        requestGuardFactories.values().stream()
                .map(RequestGuardFactoryGenerator::getField)
                .forEach(builder::addField);

        ControllerGenerator controllerGenerator =
                new ControllerGenerator
                        (controller.getElement().asType(), "controller",
                                endpoint.getMethodName(), endpoint.getReturnType());

        builder.addField(controllerGenerator.getField());

        builder.addMethod(new ResolverConstructorGenerator(controllerGenerator,
                typeAdaptorGenerators, typeAdaptorFactoryGenerator, types).make());
        builder.addMethod(new ResolveMethodGenerator
                (typeAdaptorGenerators, types, typeAdaptorFactoryGenerator,
                       null ,controllerGenerator, endpoint).make());


        return builder.build();
    }

    private boolean requiresAdaptor(MethodParameter.Source source) {
        return source == MethodParameter.Source.BODY
                || source == MethodParameter.Source.QUERY
                || source == MethodParameter.Source.PATH;
    }

    private Map<TypeName, TypeAdaptorGenerator> makeTypeAdaptors(List<TypeMirror> forTypes) {
        return forTypes.stream()
                .map(t -> new TypeAdaptorGenerator(t, types))
                .collect(Collectors.toMap(a -> TypeName.get(a.getAdaptingType()), Function.identity()));
    }

    private Map<TypeName, RequestGuardFactoryGenerator> makeRequestGuardFactories(List<TypeMirror> forTypes) {
        return forTypes.stream()
                .map(t -> new RequestGuardFactoryGenerator(requestGuards.getRequestGuard((DeclaredType) t), types))
                .collect(Collectors.toMap(a -> TypeName.get(a.getProducingType()), Function.identity()));

    }

    private List<TypeMirror> getAllRequiredTypes() {
        List<TypeMirror> requiredTypes = new ArrayList<>();

        TypeMirror returnType = endpoint.getReturnTypeParameter();
        List<MethodParameter> methodParameters = new ArrayList<>(endpoint.getMethodParameters().values());

        requiredTypes.add(returnType);
        for (MethodParameter parameter : methodParameters) {
            TypeMirror type = parameter.getType();
            if (MoreTypes.isTypeMirrorOfClass(type, Request.class)) {
                continue; //no requirement for adapting Request - it's provided
            }
            if (MoreTypes.isTypeOptional(type)) {
                type = types.extractOptionalType((DeclaredType) type);
            }
            TypeMirror erased = types.erasure(type);
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
