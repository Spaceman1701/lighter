package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;

public interface Readable extends TypedElement {
    CodeBlock makeReadStub();
}
