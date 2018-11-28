package fun.connor.lighter.handler;


/**
 * Marker interface that identifies a type as a Request Guard.
 * <p>
 *     Request Guards are special objects that used to signify endpoint method
 *     decencies. Just like path or query parameters, Request Guard dependencies
 *     are declared by adding a parameter to an endpoint method signature. However,
 *     unlike other endpoint method dependencies, Request Guards are not mapped directly
 *     from the request. Instead, Request Guards are created using user defined
 *     {@link RequestGuardFactory}s. This allows the application developer to build
 *     domain specific logic for constructing endpoint dependencies. Much of the behavior
 *     that could be implemented using request filters or chained handlers can be implemented
 *     this way.
 * </p>
 * <p>
 *     Request Guards are preferable to these other approaches because they are directly declared
 *     as dependencies in the method signature. It is impossible for a method to execute with
 *     first constructing the required Request Guards. Thus, it is impossible to incorrectly configure
 *     the application to cause improper behavior.
 * </p>
 * <p>
 *     In addition to this, Request Guards act as witnesses to their own construction. Any code that
 *     obtains a valid instance of a Request Guard can be certain that the conditions for its construction
 *     were met. Otherwise, the Request Guard could never be instantiated.
 * </p>
 * <p>
 *     Thus, RequestGuards and {@link RequestGuardFactory}s can be used to add necessary prerequisites
 *     to endpoint execution that is still decoupled with the execution. The RequestGuard and its factories
 *     can be thought of as acting on their own domain. This domain is defined by the RequestGuard class
 *     itself.
 * </p>
 * <p>
 *     Examples of idiomatic use cases for RequestGuards are authentication, request validation,
 *     and reading cookies. Never use RequestGuards to send arbitrary data to the handler.
 * </p>
 */
public interface RequestGuard {
}
