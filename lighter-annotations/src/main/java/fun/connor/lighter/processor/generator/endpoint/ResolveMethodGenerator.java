package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.LocalVariable;
import fun.connor.lighter.processor.generator.codegen.MethodParameterGenerator;
import fun.connor.lighter.processor.generator.codegen.TypeAdaptorGenerator;
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

    public ResolveMethodGenerator
            (Map<TypeName, TypeAdaptorGenerator> adaptorGenerators, LighterTypes types,
             ControllerGenerator controller, Endpoint endpoint) {
        this.adaptorGenerators = adaptorGenerators;
        this.types = types;
        this.endpoint = endpoint;
        this.controller = controller;
    }

    public MethodSpec make() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(TypeName.get(endpoint.getReturnType()));

        List<MethodParameterGenerator> parameters = makeMethodParameters();
        builder.addParameters(parameters.stream().map(MethodParameterGenerator::getParameterSpec).collect(Collectors.toList()));

        MethodParameterGenerator paramMapMaker = parameters.get(0);
        MethodParameterGenerator queryMapMaker = parameters.get(0);
        RequestGenerator requestMaker = new RequestGenerator(parameters.get(0), types);

        Map<String, MethodParameter> controllerParameters = endpoint.getMethodParameters();
        Map<String, LocalVariable> controllerParamVariables = makeControllerParamVariables(controllerParameters);



        return builder.build();
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

        TypeMirror mapStrStrType = types.mirrorOfParamterizedClass(Map.class, String.class, String.class);
        TypeMirror requestType = types.mirrorOfClass(Request.class);

        parameters.add(new MethodParameterGenerator(mapStrStrType, "paramMap_str"));
        parameters.add(new MethodParameterGenerator(mapStrStrType, "queryMap_str"));
        parameters.add(new MethodParameterGenerator(requestType, "request_raw"));

        return parameters;
    }
}
