package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

/**
 * A Java source code element that can be assigned to.
 */
public interface Assignable extends TypedElement {

    /**
     * Creates a {@link CodeBlock} that contains the Java source code stub
     * which can be directly assigned to.
     * @return the source stub
     */
    CodeBlock makeAssignmentStub();
}
