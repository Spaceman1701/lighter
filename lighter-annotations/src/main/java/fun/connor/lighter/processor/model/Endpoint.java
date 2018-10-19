package fun.connor.lighter.processor.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Endpoint {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    public static class EndpointParam {
        private String nameInMap;
        private TypeMirror type;
        private String nameOnMethod;

        private EndpointParam(String nameInMap, String nameOnMethod, TypeMirror type) {
            this.nameInMap = nameInMap;
            this.nameOnMethod = nameOnMethod;
            this.type = type;
        }

        public String getNameInMap() {
            return nameInMap;
        }

        public String getNameOnMethod() {
            return nameOnMethod;
        }

        public TypeMirror getType() {
            return type;
        }

    }

    private Method httpMethod;
    private Route fullRoute;

    private QueryParams queryParams;

    private ExecutableElement methodElement;
    private TypeMirror returnType;
    private Map<String, TypeMirror> endpointParamTypes;

    public Endpoint
            (Method httpMethod, Route fullRoute,
             QueryParams queryParams, ExecutableElement methodElement) {
        this.httpMethod = httpMethod;
        this.fullRoute = fullRoute;
        this.queryParams = queryParams;
        this.methodElement = methodElement;

        endpointParamTypes = new HashMap<>();
        extractMethodParams();
    }

    private void extractMethodParams() {
        returnType = methodElement.getReturnType();
        ExecutableType methodType = (ExecutableType) methodElement.asType();

        List<? extends VariableElement> parameterVars = methodElement.getParameters();
        List<? extends TypeMirror> parameterTypes = methodType.getParameterTypes();

        for (int i = 0; i < parameterTypes.size(); i++) {
            String name = parameterVars.get(i).getSimpleName().toString();
            TypeMirror type = parameterTypes.get(i);
            endpointParamTypes.put(name, type);
        }
    }

    public String getMethodName() {
        return methodElement.getSimpleName().toString();
    }

    public String pathTemplate() {
        return fullRoute.getTemplateStr();
    }

    public Method getHttpMethod() {
        return httpMethod;
    }

    public List<EndpointParam> getRequiredParams() {
        return getParamFromMapping(fullRoute.getParams());
    }

    public List<EndpointParam> getOptionalParams() {
        if (queryParams == null) {
            return new ArrayList<>();
        }
        return getParamFromMapping(queryParams.getNameMappings());
    }


    private List<EndpointParam> getParamFromMapping(Map<String, String> mapping) {
        List<EndpointParam> requiredParams = new ArrayList<>();

        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            String nameInMap = entry.getKey();
            String nameOnMethod = entry.getValue();
            TypeMirror type = endpointParamTypes.get(nameOnMethod);
            requiredParams.add(new EndpointParam(nameInMap, nameOnMethod, type));
        }

        return requiredParams;
    }

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }

    public List<String> getMethodArgs() {
        return methodElement.getParameters().stream()
                .map(VariableElement::getSimpleName)
                .map(Name::toString)
                .collect(Collectors.toList());
    }
}
