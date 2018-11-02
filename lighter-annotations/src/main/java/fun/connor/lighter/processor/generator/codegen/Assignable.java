package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;

public interface Assignable extends TypedElement {
    CodeBlock makeAssignmentStub();
}
