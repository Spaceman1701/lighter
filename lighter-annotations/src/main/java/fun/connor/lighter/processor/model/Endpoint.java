package fun.connor.lighter.processor.model;

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

    private MethodParameter bodyParameter;

    public Endpoint
            (Method httpMethod, Route fullRoute,
             QueryParams queryParams, String bodyParamName, ExecutableElement methodElement) {
        this.httpMethod = httpMethod;
        this.fullRoute = fullRoute;
        this.queryParams = queryParams;
        this.methodElement = methodElement;

        endpointParamTypes = new HashMap<>();
        extractMethodParams();

        this.bodyParameter = makeBodyParam(bodyParamName);
    }

    private MethodParameter makeBodyParam(String name) {
        if (name != null) {
            int index  = getMethodParamIndexByName(name);
            return new MethodParameter(index, endpointParamTypes.get(name), name);
        }
        return null;
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

    private int getMethodParamIndexByName(String name) {
        int index = 0;
        for (VariableElement element : methodElement.getParameters()) {
            if (element.getSimpleName().toString().equals(name)) {
                return index;
            }
            index++;
        }
        return -1;
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
        List<EndpointParam> path = getParamFromMapping(fullRoute.getParams(), EndpointParam.Location.PATH);

        if (queryParams != null) {
            List<EndpointParam> query = getParamFromMapping(queryParams.getNameMappings(), EndpointParam.Location.QUERY)
                    .stream()
                    .filter(p -> !isTypeOptional(p.type))
                    .collect(Collectors.toList());

            path.addAll(query);
        }

        return path;
    }

    public List<EndpointParam> getOptionalParams() {
        if (queryParams == null) {
            return new ArrayList<>();
        }
        return getParamFromMapping(queryParams.getNameMappings(), EndpointParam.Location.QUERY).stream()
                .filter(p -> isTypeOptional(p.type))
                .collect(Collectors.toList());
    }

    private boolean isTypeOptional(TypeMirror type) {
        if (type.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) type;
            TypeElement element = (TypeElement) declaredType.asElement();
            return (element.getQualifiedName().toString().equals(Optional.class.getCanonicalName()));
        }
        return false;
    }

    private List<EndpointParam> getParamFromMapping(Map<String, String> mapping, EndpointParam.Location location) {
        List<EndpointParam> requiredParams = new ArrayList<>();

        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            String nameInMap = entry.getKey();
            System.out.println(nameInMap);
            String nameOnMethod = entry.getValue();
            TypeMirror type = endpointParamTypes.get(nameOnMethod);
            requiredParams.add(new EndpointParam(nameInMap, nameOnMethod, type, location));
        }

        return requiredParams;
    }

    public List<TypeMirror> getMethodArgumentTypes() {
        return endpointParamTypes.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public MethodParameter getBodyParameter() {
        return bodyParameter;
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

    public String getSimplePathTemplate() {
        return fullRoute.getTemplateWithSimpleNames();
    }
}
