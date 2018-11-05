package fun.connor.lighter.processor.generator;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.Field;
import fun.connor.lighter.processor.generator.endpoint.*;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.RequestGuards;
import fun.connor.lighter.processor.model.endpoint.MethodParameter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EndpointResolverGenerator extends AbstractGenerator {

    private Endpoint endpoint;
    private Controller controller;
    private RequestGuards requestGuards;
    private LighterTypes types;

    private TypeSpec.Builder builder;

    public EndpointResolverGenerator(Controller controller, Endpoint endpoint,
                                     RequestGuards requestGuards, LighterTypes types, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;
        this.requestGuards = requestGuards;
        this.types = types;

        builder = TypeSpec.classBuilder(makeClassName())
                .addSuperinterface(LighterRequestResolver.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    protected TypeSpec generateType() {
        Map<MethodParameter.Source, List<MethodParameter>> params =
                endpoint.getParametersBySource();


        Map<TypeName, TypeAdaptorGenerator> typeAdaptorGenerators = makeTypeAdaptorsMap(params);
        attachFields(typeAdaptorGenerators.values());

        TypeAdaptorFactoryGenerator typeAdaptorFactoryGenerator
                = new TypeAdaptorFactoryGenerator("typeAdaptorFactory", types);
        builder.addField(typeAdaptorFactoryGenerator.getField());


        Map<TypeName, RequestGuardFactoryGenerator> requestGuardFactories = makeRequestGuardFactories(params);
        attachFields(requestGuardFactories.values());

        ControllerGenerator controllerGenerator =
                new ControllerGenerator
                        (controller.getElement().asType(), "controller",
                                endpoint.getMethodName(), endpoint.getReturnType());

        builder.addField(controllerGenerator.getField());

        builder.addMethod(new ResolverConstructorGenerator(controllerGenerator,
                typeAdaptorGenerators, typeAdaptorFactoryGenerator, requestGuardFactories, types).make());
        builder.addMethod(new ResolveMethodGenerator
                (typeAdaptorGenerators, types, typeAdaptorFactoryGenerator,
                       requestGuardFactories ,controllerGenerator, endpoint).make());


        return builder.build();
    }

    private void attachFields(Collection<? extends Field> fields) {
        fields.stream()
                .map(Field::getField)
                .forEach(builder::addField);
    }

    private Map<TypeName, TypeAdaptorGenerator> makeTypeAdaptorsMap
            (Map<MethodParameter.Source, List<MethodParameter>> params) {
        List<MethodParameter> parametersForAdapting = params.entrySet().stream()
                .filter(e -> requiresAdaptor(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return makeObjectFromTypes(parametersForAdapting, (t) -> new TypeAdaptorGenerator(t, types));
    }

    private Map<TypeName, RequestGuardFactoryGenerator> makeRequestGuardFactories
            (Map<MethodParameter.Source, List<MethodParameter>> params) {
        List<MethodParameter> typesForGuards =
                new ArrayList<>(params.getOrDefault(MethodParameter.Source.GUARD, new ArrayList<>()));
        return makeObjectFromTypes(typesForGuards, (t) ->
                        new RequestGuardFactoryGenerator(requestGuards.getRequestGuard((DeclaredType)t), types));
    }

    private boolean requiresAdaptor(MethodParameter.Source source) {
        return source == MethodParameter.Source.BODY
                || source == MethodParameter.Source.QUERY
                || source == MethodParameter.Source.PATH;
    }

    private <T> Map<TypeName, T> makeObjectFromTypes(List<MethodParameter> forTypes, Function<TypeMirror, T> constructor) {
        Map<TypeName, T> results = new HashMap<>();
        for (MethodParameter parameter : forTypes) {
            TypeMirror type = parameter.getType();
            if (parameter.isOptional()) {
                type = types.extractOptionalType((DeclaredType) type);
            }
            if (type.getKind().isPrimitive()) {
                type = types.boxedClass((PrimitiveType) type).asType();
            }
            TypeName typeName = TypeName.get(type);
            if (!results.containsKey(typeName)) {
                results.put(typeName, constructor.apply(type));
            }
        }
        return results;
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
