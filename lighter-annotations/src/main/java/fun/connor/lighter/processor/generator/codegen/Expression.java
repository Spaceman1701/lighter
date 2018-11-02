package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;

public interface Expression extends TypedElement, Statement {
    CodeBlock makeReadStub();

    @Override
    default CodeBlock make() {
        return makeReadStub();
    }
}
