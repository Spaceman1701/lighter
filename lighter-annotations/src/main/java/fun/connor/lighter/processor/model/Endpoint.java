package fun.connor.lighter.processor.model;

import fun.connor.lighter.processor.MoreTypes;
import fun.connor.lighter.processor.model.endpoint.MethodParameter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.stream.Collectors;

public class Endpoint {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    public static class EndpointParam {
        public enum Location {
            PATH, QUERY
        }
        private String nameInMap;
        private TypeMirror type;
        private String nameOnMethod;
        private Location location;

        private EndpointParam(String nameInMap, String nameOnMethod, TypeMirror type, Location location) {
            this.nameInMap = nameInMap;
            this.nameOnMethod = nameOnMethod;
            this.type = type;
            this.location = location;
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

        public Location getLocation() {
            return location;
        }
    }

    private Method httpMethod;
    private Route fullRoute;

    private QueryParams queryParams;

    private ExecutableElement methodElement;
    private TypeMirror returnType;
    private Map<String, TypeMirror> endpointParamTypes;
    private Map<String, MethodParameter> methodParameters;


    public Endpoint
            (Method httpMethod, Route fullRoute,
             QueryParams queryParams, String bodyParamName,
             String contextParamName, ExecutableElement methodElement) {
        this.httpMethod = httpMethod;
        this.fullRoute = fullRoute;
        this.queryParams = queryParams;
        this.methodElement = methodElement;

        endpointParamTypes = new HashMap<>();
        methodParameters = new HashMap<>();
        extractMethodParams(bodyParamName, contextParamName);

    }

    private void extractMethodParams(String bodyParamName, String contextParamName) {
        returnType = methodElement.getReturnType();
        ExecutableType methodType = (ExecutableType) methodElement.asType();

        List<? extends VariableElement> parameterVars = methodElement.getParameters();
        List<? extends TypeMirror> parameterTypes = methodType.getParameterTypes();

        for (int i = 0; i < parameterTypes.size(); i++) {
            String name = parameterVars.get(i).getSimpleName().toString();
            TypeMirror type = parameterTypes.get(i);
            endpointParamTypes.put(name, type);
            methodParameters.put(name, makeMethodParam(i, type, name, bodyParamName, contextParamName));
        }
    }

    private MethodParameter makeMethodParam
            (int index, TypeMirror type, String name, String bodyParamName, String contextParamName) {
        MethodParameter.Source source;
        if (name.equals(contextParamName)) {
            source = MethodParameter.Source.CONTEXT;
        } else if (name.equals(bodyParamName)) {
            source = MethodParameter.Source.BODY;
        } else if (queryParams != null && queryParams.containsValue(name)) {
            source = MethodParameter.Source.QUERY;
        } else {
            source = MethodParameter.Source.PATH;
        }
        return new MethodParameter(index, type, name, source);
    }

    public String getQueryParamName(String methodParamName) {
        return reverseParameterLookup(methodParamName, queryParams.getNameMappings());
    }

    public String getPathParamName(String methodParamName) {
        return reverseParameterLookup(methodParamName, fullRoute.getParams());
    }

    private String reverseParameterLookup(String methodParamName, Map<String, String> nameMapping) {
        for (Map.Entry<String, String> entry : nameMapping.entrySet()) {
            if (entry.getValue().equals(methodParamName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Map<String, MethodParameter> getMethodParameters() {
        return methodParameters;
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


    public List<TypeMirror> getMethodArgumentTypes() {
        return endpointParamTypes.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }

    public TypeMirror getReturnTypeParameter() {
        return ((DeclaredType)returnType).getTypeArguments().get(0);
    }


    public String getSimplePathTemplate() {
        return fullRoute.getTemplateWithSimpleNames();
    }
}
