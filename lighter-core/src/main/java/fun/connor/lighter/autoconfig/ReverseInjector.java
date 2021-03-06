package fun.connor.lighter.autoconfig;

import fun.connor.lighter.injection.InjectionObjectFactory;

/**
 * The reverse injector allows injection frameworks which cannot
 * produce an InjectorObjectFactory to inject into Lighter directly.
 *<br><br>
 * It uses {@link javax.inject.Inject} to mark java-beans compatible setter methods
 * for every object the Lighter compiler determines is required as a directly constructed
 * dependency.
 *<br><br>
 * Right now, there are two types of objects that fall into this category
 *<br><br>
 * 1) {@link fun.connor.lighter.declarative.ResourceController}
 * <br>
 * 2) {@link fun.connor.lighter.handler.RequestGuardFactory}
 * <br><br>
 *
 * While it is possible to write implementations of this interface by hand, Lighter will generate
 * one for you. This will be accessible through the {@code AutoConfigFactory}
 * link other generated classes.
 *
 * Example:
 * <pre> {@code
 *     public class ExampleLighterReverseInjector implements ReverseInjector {
 *
 *         {@literal @}Override
 *         public InjectionObjectFactory getInjector() {
 *             ...
 *         }
 *
 *         {@literal @}Inject
 *         public ExampleController setExampleController(ExampleController ex) {
 *             ...
 *         }
 *
 *         ...
 *     }
 *     }
 * </pre>
 */
public interface ReverseInjector {

    InjectionObjectFactory getInjector();
}
