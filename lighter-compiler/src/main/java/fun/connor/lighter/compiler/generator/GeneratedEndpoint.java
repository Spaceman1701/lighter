package fun.connor.lighter.compiler.generator;

import com.squareup.javapoet.TypeName;
import fun.connor.lighter.compiler.model.Endpoint;

/**
 * Represents an already generated Java class that was created from an {@link Endpoint}.
 */
public class GeneratedEndpoint {

    private Endpoint endpoint;
    private GeneratedType generatedType;

    /**
     * Construct a GeneratedEndpoint
     * @param endpoint an endpoint that was used to generate source code
     * @param generatedType the generate type itself
     */
    public GeneratedEndpoint(Endpoint endpoint, GeneratedType generatedType) {
        this.endpoint = endpoint;
        this.generatedType = generatedType;
    }

    /**
     * Get the type name of the {@link fun.connor.lighter.handler.LighterRequestResolver} implementation
     * @return the type name
     */
    public TypeName getHandlerType() {
        return generatedType.getTypeName();
    }

    /**
     * Get the HTTP method that the LighterRequestResolver should be able to respond to
     * @return the HTTP method string
     */
    public String getHttpMethod() {
        return endpoint.getHttpMethod().toString();
    }

    /**
     * Get the route template that the LighterRequestResolver should handle
     * @return the route template string
     */
    public String getRouteTemplate() {
        return endpoint.getSimplePathTemplate();
    }
}
