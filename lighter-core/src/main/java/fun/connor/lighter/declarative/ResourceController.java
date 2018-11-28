package fun.connor.lighter.declarative;

import java.lang.annotation.*;

/**
 * Identifies the annotated class as a Resource Controller. Resource Controllers are the only types which can
 * have endpoint handler methods. Typically, a Resource Controller represents a controller for a specific type
 * in the application domain. Resource Controllers must provide a unique path fragment that constrains the
 * paths of its endpoint handler methods. The path template fragment defined by the Resource Controller is prepended
 * to the path templates of each of its endpoint handler methods.
 * <p>
 * Path templates follow a simple syntax. For paths that do not have any parameters, the template looks exactly like
 * the path. For example, the path template:
 * <br><br>
 * &nbsp; &nbsp; /foo/bar/123
 * <br><br>
 * will match only the exact path
 * <br><br>
 * &nbsp; &nbsp; /foo/bar/123
 * <br><br>
 * For paths with parameters, use curly braces ({}) wrapped around the parameter name to identify it as a parameter.
 * Parameters are bound to endpoint handler method parameters that have the same name. No two parameters in a path
 * can have the same name. Legal names for path parameters are the same as legal names for Java method parameters.
 * There is no way to identify the type of the parameter in the path template. Instead, Lighter infers the template
 * parameter type from the method signature of endpoint handler method. Thus, paths cannot be differentiated
 * from eachother using the type or name of their template parameters.<br>
 *
 * An example of path with parameters:
 *<br><br>
 *&nbsp; &nbsp; /foo/bar/{id}
 *<br><br>
 *This template will match any path that starts with /foo/bar and has exactly 3 components. The third component of the
 *path will be bound to the method parameter with the name "id". Lighter will use the configured
 * {@link fun.connor.lighter.adapter.TypeAdapter} to attempt to deserialize the parameter from the path. Path parameters
 * are assigned a IANA Media type of "text/plain".
 * </p>
 * <p>
 * All of these rules are enforced by the compiler.
 * </p>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceController {
    /**
     * Defines the path fragment which this method can respond to. Defaults to an empty path.
     */
    String value();
}
