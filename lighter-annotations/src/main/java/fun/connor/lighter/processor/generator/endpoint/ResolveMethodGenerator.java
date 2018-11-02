package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.*;
import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.endpoint.MethodParameter;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResolveMethodGenerator {

    private Map<TypeName, TypeAdaptorGenerator> adaptorGenerators;
    private LighterTypes types;
    private Endpoint endpoint;
    private ControllerGenerator controller;

    private List<MethodParameterGenerator> parameterGenerators;
    private MapGenerator paramMapMaker;
    private MapGenerator queryMapMaker;
    private RequestGenerator requestMaker;

    private Map<String, MethodParameter> controllerParams;
    private Map<String, LocalVariable> controllerParamVariables;

    public ResolveMethodGenerator
            (Map<TypeName, TypeAdaptorGenerator> adaptorGenerators, LighterTypes types,
             ControllerGenerator controller, Endpoint endpoint) {
        this.adaptorGenerators = adaptorGenerators;
        this.types = types;
        this.endpoint = endpoint;
        this.controller = controller;

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

        return builder.build();
    }

    private CodeBlock makeParameterAssignments() {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (Map.Entry<String, MethodParameter> param : controllerParams.entrySet()) {
            if (param.getValue().getSource() == MethodParameter.Source.CONTEXT) {
                Assignable assignable = controllerParamVariables.get(param.getKey());
                Expression contextCreateExpr = makeContextFromRequest();
                builder.addStatement(Assignment.of(assignable, contextCreateExpr).make());
            } else {
                builder.add(makeVariableMarshaling(param.getValue(), controllerParamVariables.get(param.getKey())));
            }
        }
        return builder.build();
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
            case CONTEXT:
                throw new IllegalArgumentException("context param doesn't require marshalling");
        }
        throw new IllegalArgumentException("unexpected parameter source type");
    }

    private Expression makeContextFromRequest() {
        return requestMaker;
    }

    private CodeBlock makeVariableMarshaling(MethodParameter param, LocalVariable output) {
        Expression getParam = getMarshallerSource(param);
        MethodParamMarshalGenerator marshalGenerator =
                new MethodParamMarshalGenerator(output, getParam, adaptorGenerators, types);
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
