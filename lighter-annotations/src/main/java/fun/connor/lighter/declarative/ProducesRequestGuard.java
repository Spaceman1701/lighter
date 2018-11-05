package fun.connor.lighter.declarative;

import fun.connor.lighter.handler.RequestGuard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a RequestGuard factory AND the
 * factory methods on it
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducesRequestGuard {
    Class<? extends RequestGuard> value();
}
