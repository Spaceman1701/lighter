package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

/**
 * A Java source code element that does not have a value and cannot be assigned to. An example
 * of a statement is a single line of code. Statements can also represent blocks of code which do
 * not return a value, void methods, and variable declarations.
 */
@FunctionalInterface
public interface Statement {
    /**
     * Build a snippet of Java source code that represents this statement.
     * @return a {@link CodeBlock} with this statement as Java source code.
     */
    CodeBlock make();
}
