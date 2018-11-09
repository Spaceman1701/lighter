package fun.connor.lighter.compiler.generator;

import com.squareup.javapoet.TypeName;
import fun.connor.lighter.compiler.model.Endpoint;

public class GeneratedEndpoint {

    private Endpoint endpoint;
    private GeneratedType generatedType;

    public GeneratedEndpoint(Endpoint endpoint, GeneratedType generatedType) {
        this.endpoint = endpoint;
        this.generatedType = generatedType;
    }

    public TypeName getHandlerType() {
        return generatedType.getTypeName();
    }

    public String getHttpMethod() {
        return endpoint.getHttpMethod().toString();
    }

    public String getRouteTemplate() {
        return endpoint.getSimplePathTemplate();
    }
}
