/**
 * Runtime API for compile-time generated automatic configuration. This includes router and
 * dependency configuration classes. Notably, the interfaces defined in this API do not
 * depend on lighter-compiler's specific implementation. This allows application developers
 * to write their own configuration objects if there are special requirements.
 * <br>
 * For example, the APIs in this package can be used to hand-write endpoint handler wiring or manual
 * dependency management.
 */
package fun.connor.lighter.autoconfig;