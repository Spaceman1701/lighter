/**
 * The Lighter Compiler implementation. This library contains all the components for Lighter's compile
 * time validation and code generation. This library is a <strong>compile time only</strong> dependency for
 * consumers of Lighter. The Lighter runtime is contained within <code>lighter-core</code> which is
 * also a dependency of this library.
 *<br>
 * This library is organized the following way:
 *  <ul>
 *      <li>At the top level, the {@link fun.connor.lighter.compiler.LighterAnnotationProcessor} and
 *      utility classes</li>
 *      <li>{@link fun.connor.lighter.compiler.generator} package containing code-generation implementations</li>
 *      <li>{@link fun.connor.lighter.compiler.model} package containing the compile-time generated application model domain objects</li>
 *      <li>{@link fun.connor.lighter.compiler.step} package containing the high-level compiler steps which encapsulate individual
 *      compile-time behaviors</li>
 *      <li>{@link fun.connor.lighter.compiler.validation} package containing error-reporting logic</li>
 *      <li>{@link fun.connor.lighter.compiler.validators} package (to be renamed) containing annotation-validators</li>
 *  </ul>
 *
 * <br>
 * The Lighter Compiler works using an iterative-step approach. The compiler's behavior is broken up into a sequence
 * of small steps which each provide a specific explicit or implicit refinement to the data model. In practice, there
 * are two types of steps: validation steps and generation steps. Validation steps usually provide an implicit refinement
 * to the model by ensuring that certain invariants are enforced before the next generation step occurs. Generation steps
 * explicitly rebuild the model using the new invariants provided by each validation.
 * <br>
 * Currently Lighter has 7 compilation steps. The implementations can be found in the package linked above. The order
 * can be found in {@link fun.connor.lighter.compiler.LighterAnnotationProcessor}. On a high level, the sequence is
 * approximately as follows:
 * <ol>
 *     <li>Each Lighter annotation is validated independently. Argument syntax and correctness is checked</li>
 *     <li>The general application model is generated from the set of all Lighter-annotated classes</li>
 *     <li>The request guard provisioning model is generated from the set of request guard annotations</li>
 *     <li>The general application model is validated for semantic correctness</li>
 *     <li>Request guard dependencies are collected and stored for lookup</li>
 *     <li>Application endpoint classes and controller metadata classes are generated from the model</li>
 *     <li>The reverse injector class for the module is generated from the generated endpoint classes</li>
 * </ol>
 */
package fun.connor.lighter.compiler;