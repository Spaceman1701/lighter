package fun.connor.lighter.declarative;

import java.lang.annotation.*;

/**
 * Defines bindings for request query parameters to endpoint handler method parameters. This annotation
 * can only be placed on methods that are annotated with a Endpoint Definition Annotation.
 * <p>
 *     Each element of the parameter array represents a single query parameter. As with path parameters, the
 *     types for query parameters are deduced from the types of the method parameters they map to.
 * </p>
 * <p>
 *     Since query
 *     parameter names must be exposed to consumers of the application's API, it is possible to map query parameters
 *     to method parameters with a different name. This can be done by following the query parameter name with a colon
 *     and then the name of the method parameter it should be bound to. The name of the query parameter in the request
 *     is called the <strong>exposed name</strong>. The name of the query parameter in the Java method is called the
 *     <strong>mapped name</strong>. If no mapped name is provided, the exposed name and map name are considered
 *     identical.
 * </p>
 * <p>
 *     A simple use case for this is converting typical snake case query parameter names to idiomatic camelCase java
 *     parameter names:
 *     <br><br>
 *     &nbsp; &nbsp; exposed_name:exposedName
 *     <br><br>
 *     When a request contains a query parameter named "exposed_name" it will be bound to the java method parameter
 *     "exposedName"
 * </p>
 * <p>
 *     Every query parameter on a method must have a unique exposed name and a unique mapped name.
 * </p>
 * <p>
 *     Query parameters <strong>are not used</strong> in resolving requests. Query parameters cannot be used to
 *     differentiate endpoints. Similarly, query parameters present in a request that are not defined with a
 *     QueryParams annotation are ignored.
 * </p>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParams {
    /**
     * The list of possible query parameters for this method
     */
    String[] value();
}
