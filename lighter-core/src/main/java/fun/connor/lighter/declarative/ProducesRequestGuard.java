package fun.connor.lighter.declarative;

import fun.connor.lighter.handler.RequestGuard;

import java.lang.annotation.*;

/**
 * Identifies a {@link fun.connor.lighter.handler.RequestGuardFactory} as producing
 * a specific {@link RequestGuard} type for the application. Every RequestGuardFactory
 * marked with this annotation must produce a unique RequestGuard type.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducesRequestGuard {
    /**
     * The type which this factory should be used to produce
     */
    Class<? extends RequestGuard> value();
}
