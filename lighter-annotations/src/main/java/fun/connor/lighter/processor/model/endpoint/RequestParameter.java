package fun.connor.lighter.processor.model.endpoint;

public class RequestParameter {
    private MethodParameter methodParameter;
    private String nameInMap;

    public RequestParameter(MethodParameter methodParameter, String nameInMap) {
        this.methodParameter = methodParameter;
        this.nameInMap = nameInMap;
    }

    public MethodParameter getMethodParameter() {
        return methodParameter;
    }

    public String getNameInMap() {
        return nameInMap;
    }
}
