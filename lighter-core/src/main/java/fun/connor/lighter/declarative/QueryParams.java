package fun.connor.lighter.declarative;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParams {
    String[] value();
}
