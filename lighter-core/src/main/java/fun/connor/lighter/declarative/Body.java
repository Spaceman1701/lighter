package fun.connor.lighter.declarative;

import java.lang.annotation.*;

/**
 * Binds the value of the annotated parameter to the request body
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Body {

}
