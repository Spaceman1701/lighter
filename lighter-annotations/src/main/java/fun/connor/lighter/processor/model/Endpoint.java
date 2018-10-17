package fun.connor.lighter.processor.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Endpoint {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    private Method httpMethod;
    private Route fullRoute;

    private QueryParamsProcessor queryParams;

    private ExecutableElement methodElement;
    private TypeMirror returnType;
    private Map<String, TypeMirror> endpointParamTypes;

    public Endpoint
            (Method httpMethod, Route fullRoute,
             QueryParamsProcessor queryParams, ExecutableElement methodElement) {
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
}
