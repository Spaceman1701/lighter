package fun.connor.lighter.compiler.model;

import fun.connor.lighter.compiler.model.endpoint.MethodParameter;
import fun.connor.lighter.compiler.model.validators.Validators;
import fun.connor.lighter.compiler.validation.LocationHint;
import fun.connor.lighter.compiler.validation.Validatable;
import fun.connor.lighter.compiler.validation.ValidationReport;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public class Endpoint implements Validatable {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    private Method httpMethod;
    private Route fullRoute;

    private QueryParams queryParams;

    private ExecutableElement methodElement;
    private TypeMirror returnType;
    private Map<String, MethodParameter> methodParameters;


    public Endpoint
            (Method httpMethod, Route fullRoute,
             QueryParams queryParams, String bodyParamName,
             String contextParamName, ExecutableElement methodElement) {
        this.httpMethod = httpMethod;
        this.fullRoute = fullRoute;
        this.queryParams = queryParams;
        this.methodElement = methodElement;

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
        } else if (fullRoute.getParams().containsValue(name)){
            source = MethodParameter.Source.PATH;
        } else {
            source = MethodParameter.Source.GUARD;
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

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }

    public String getSimplePathTemplate() {
        return fullRoute.getTemplateWithSimpleNames();
    }

    public Map<MethodParameter.Source, List<MethodParameter>> getParametersBySource() {
        Map<MethodParameter.Source, List<MethodParameter>> result = new HashMap<>();
        Collection<MethodParameter> parameters = methodParameters.values();

        for (MethodParameter p : parameters) {
            MethodParameter.Source s = p.getSource();
            if (!result.containsKey(s)) {
                result.put(s, new ArrayList<>());
            }
            result.get(s).add(p);
        }
        return result;
    }

    /**
     * Semantic equality between two endpoints.
     * <br>
     * Endpoints are the same if either:
     * <br>
     * 1) They are equal (see {@link Endpoint#equals(Object)})
     * <br>
     * 2) Both endpoint's routes capture each other (see {@link Route#captures(Route)}) and
     * they have the same HTTP method
     * @return <code>true</code> iff this endpoint is the same as the other
     */
    public boolean isSameEndpoint(Endpoint other) {
        if (other == null) {
            return false;
        }
        if (equals(other)) {
            return true;
        }
        return other.getHttpMethod().equals(this.getHttpMethod())
                && other.fullRoute.captures(fullRoute);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Endpoint)) {
            return false;
        }
        Endpoint that = (Endpoint) o;

        return methodElement.equals(that.methodElement);
    }

    @Override
    public int hashCode() {
        return methodElement.hashCode();
    }

    public String getFullName() {
        String methodName = getMethodName();
        TypeElement containingType = (TypeElement) methodElement.getEnclosingElement(); //method must be in class
        String className = containingType.getQualifiedName().toString();
        return className + "#" + methodName;
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        LocationHint hint = new LocationHint(methodElement);

        ValidationReport.Builder pathParamsReport = ValidationReport.builder(hint);
        Validators.Endpoint.allParamsExist(fullRoute.getParams(), methodParameters).validate(pathParamsReport);
        reportBuilder.addChild(pathParamsReport);

        if (queryParams != null) {
            ValidationReport.Builder queryParamsReport = ValidationReport.builder(hint);
            Validators.Endpoint.allParamsExist(queryParams.getNameMappings(), methodParameters).validate(reportBuilder);
            reportBuilder.addChild(queryParamsReport);
        }

        Validators.Endpoint.allParametersUnique(fullRoute.getParams(), queryParams).validate(reportBuilder);
    }
}
