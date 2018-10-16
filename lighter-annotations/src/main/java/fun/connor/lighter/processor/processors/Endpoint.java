package fun.connor.lighter.processor.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;

public class Endpoint {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    private ProcessingEnvironment env;
    private RoundEnvironment roundEnv;

    private Method httpMethod;
    private Route fullRoute;

    private QueryParams queryParams;

    private ExecutableElement methodElement;
    private TypeMirror returnType;
    private Map<String, TypeMirror> endpointParamTypes;

    public Endpoint
            (Method httpMethod, Route fullRoute,
             QueryParams queryParams, ExecutableElement methodElement,
             ProcessingEnvironment env, RoundEnvironment roundEnv) {
        this.httpMethod = httpMethod;
        this.fullRoute = fullRoute;
        this.queryParams = queryParams;
        this.methodElement = methodElement;
        this.env = env;
        this.roundEnv = roundEnv;

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
            env.getMessager().printMessage(Diagnostic.Kind.NOTE,
                    "found method param " + name + ": " + type.toString());
        }
    }
}
