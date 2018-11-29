package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.*;
import fun.connor.lighter.compiler.model.Endpoint;
import fun.connor.lighter.compiler.model.MethodParameter;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.http.MediaType;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java source code generator for the
 * {@link fun.connor.lighter.handler.LighterRequestResolver#resolve(Map, Map, Request)} method in LighterRequestResolver
 * implementations. This class is quite complex as the resolve method is responsible for marshalling the raw request
 * into the Java type dependencies of the endpoint method it delegates to. This includes data from the path parameters,
 * query parameters, request body, and RequestGuardFactories.
 * <p>
 *     This complexity is not exposed by the interface, however. The behavior of the request method is fixed for
 *     every resolver, so there is no need to expose complex configuration of behavior.
 * </p>
 */
public class ResolveMethodGenerator {

    private Map<TypeName, RequestGuardFactoryGenerator> requestGuards;
    private TypeAdaptorFactoryGenerator typeAdaptorFactory;
    private LighterTypes types;
    private Endpoint endpoint;
    private ControllerGenerator controller;

    private List<MethodParameterGenerator> parameterGenerators;
    private MapGenerator paramMapMaker;
    private MapGenerator queryMapMaker;
    private RequestGenerator requestMaker;

    private Map<String, MethodParameter> controllerParams;
    private Map<String, LocalVariable> controllerParamVariables;

    /**
     * Construct a new resolve method generator for a specific implementation
     * @param types type utilities
     * @param typeAdapterFactory generator for the top-level {@link fun.connor.lighter.adapter.TypeAdapterFactory}.
     * @param requestGuards Map of request guard factory code generators keyed by the name of the RequestGuard type
     *                      they produce
     * @param controller generator for the controller that contains the method this resolver will delegate to
     * @param endpoint the endpoint which represents this request resolver.
     */
    public ResolveMethodGenerator
            (LighterTypes types,
             TypeAdaptorFactoryGenerator typeAdapterFactory,
             Map<TypeName, RequestGuardFactoryGenerator> requestGuards,
             ControllerGenerator controller, Endpoint endpoint) {
        this.types = types;
        this.endpoint = endpoint;
        this.controller = controller;
        this.typeAdaptorFactory = typeAdapterFactory;
        this.requestGuards = requestGuards;

        initGenerators();
    }

    private void initGenerators() {
        parameterGenerators = makeMethodParameters();

        paramMapMaker = new MapGenerator(String.class, String.class, parameterGenerators.get(0), types);
        queryMapMaker = new MapGenerator(String.class, String.class, parameterGenerators.get(1), types);
        requestMaker = new RequestGenerator(parameterGenerators.get(2), types);

        controllerParams = endpoint.getMethodParameters();
        controllerParamVariables = makeControllerParamVariables(controllerParams);
    }

    /**
     * Create the JavaPoet {@link MethodSpec} for the resolve method.
     * @return the MethodSpec
     */
    public MethodSpec make() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(TypeName.get(endpoint.getReturnType()));

        builder.addParameters(parameterGenerators
                .stream()
                .map(MethodParameterGenerator::getParameterSpec)
                .collect(Collectors.toList()));

        for (LocalVariable variable : controllerParamVariables.values()) {
            builder.addCode(variable.makeDeclaration());
        }

        builder.addCode(makeParameterAssignments());

        builder.addCode(makeReturnStatement());

        return builder.build();
    }

    private CodeBlock makeReturnStatement() {
        return CodeBlock.builder()
                .addStatement("return $L", controller.makeEndpointCall(makeOrderedMethodArgs()).makeReadStub())
                .build();
    }

    private List<Expression> makeOrderedMethodArgs() {
        List<LocalVariable> sorted = new ArrayList<>(controllerParamVariables.values());

        sorted.sort((a, b) -> {
            int aIndex = controllerParams.get(a.getName()).getIndex();
            int bIndex = controllerParams.get(b.getName()).getIndex();
            return aIndex - bIndex;
        });

        return new ArrayList<>(sorted);
    }

    private CodeBlock makeParameterAssignments() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (Map.Entry<String, MethodParameter> param : controllerParams.entrySet()) {
            if (param.getValue().getSource() == MethodParameter.Source.CONTEXT) {
                Assignable assignable = controllerParamVariables.get(param.getKey());
                Expression contextCreateExpr = makeContextFromRequest();
                builder.addStatement(Assignment.of(assignable, contextCreateExpr).make());
            } else if (param.getValue().getSource() == MethodParameter.Source.GUARD) {
                builder.add(makeGuardParam(param.getValue(), controllerParamVariables.get(param.getKey())));
            } else {
                builder.add(makeVariableMarshaling(param.getValue(), controllerParamVariables.get(param.getKey())));
            }
        }
        return builder.build();
    }

    private CodeBlock makeGuardParam(MethodParameter parameter, LocalVariable localVariable) {
        TypeMirror guardType = types.erasure(parameter.getType());
        RequestGuardFactoryGenerator generator = requestGuards.get(TypeName.get(guardType));
        Expression factoryExpr =
                generator.makeNewInstance(paramMapMaker, queryMapMaker, requestMaker, typeAdaptorFactory);

        return CodeBlock.of("$L;\n", Assignment.of(localVariable, factoryExpr).make());
    }

    private Expression getMarshallerSource(MethodParameter param) {
        switch (param.getSource()) {
            case QUERY:
                String nameInQueryMap = endpoint.getQueryParamName(param.getName());
                return queryMapMaker.makeGet(Expression.literal(String.class, nameInQueryMap, types));
            case PATH:
                String nameInParamMap = endpoint.getPathParamName(param.getName());
                return paramMapMaker.makeGet(Expression.literal(String.class, nameInParamMap, types));
            case BODY:
                return requestMaker.makeGetBody();
            case GUARD:
                throw new IllegalArgumentException("guard param doesn't require marhsalling");
            case CONTEXT:
                throw new IllegalArgumentException("context param doesn't require marshalling");
        }
        throw new IllegalArgumentException("unexpected parameter source type");
    }

    private Expression getMethodParamContentType(MethodParameter param) {
        switch (param.getSource()) {
            case QUERY:
                return Expression.literal(String.class, MediaType.TEXT_PLAIN, types);
            case PATH:
                return Expression.literal(String.class, MediaType.TEXT_PLAIN, types);
            case BODY:
                return requestMaker.makeGetContentType();
            case GUARD:
                throw new IllegalArgumentException("guard param doesn't require a content type");
            case CONTEXT:
                throw new IllegalArgumentException("context param doesn't require a content type");
        }
        throw new IllegalArgumentException("unexpected parameter source type");
    }

    private Expression makeContextFromRequest() {
        return requestMaker;
    }

    private CodeBlock makeVariableMarshaling(MethodParameter param, LocalVariable output) {
        Expression getParam = getMarshallerSource(param);
        Expression paramContentType = getMethodParamContentType(param);
        MethodParamMarshalGenerator marshalGenerator =
                new MethodParamMarshalGenerator(output, getParam, paramContentType, typeAdaptorFactory, types);
        return marshalGenerator.make();
    }

    private Map<String, LocalVariable> makeControllerParamVariables(Map<String, MethodParameter> parameters) {
        Map<String, LocalVariable> variables = new HashMap<>();
        for (Map.Entry<String, MethodParameter> entry : parameters.entrySet()) {
            MethodParameter param = entry.getValue();
            variables.put(entry.getKey(), new LocalVariable(param.getType(), param.getName()));
        }
        return variables;
    }

    private List<MethodParameterGenerator> makeMethodParameters() {
        List<MethodParameterGenerator> parameters = new ArrayList<>();

        TypeMirror mapStrStrType = types.mirrorOfParameterizedClass(Map.class, String.class, String.class);
        TypeMirror requestType = types.mirrorOfClass(Request.class);

        parameters.add(new MethodParameterGenerator(mapStrStrType, "paramMap_str"));
        parameters.add(new MethodParameterGenerator(mapStrStrType, "queryMap_str"));
        parameters.add(new MethodParameterGenerator(requestType, "request_raw"));

        return parameters;
    }
}
