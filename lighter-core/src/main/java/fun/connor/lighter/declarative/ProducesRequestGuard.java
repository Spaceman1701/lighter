package fun.connor.lighter.declarative;

import fun.connor.lighter.handler.RequestGuard;

import java.lang.annotation.*;

/**
 * Used to mark a RequestGuard factory AND the
 * factory methods on it
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducesRequestGuard {
    Class<? extends RequestGuard> value();
}
