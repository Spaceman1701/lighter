package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

public interface Assignable extends TypedElement {
    CodeBlock makeAssignmentStub();
}
