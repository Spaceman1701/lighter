package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;

@FunctionalInterface
public interface Statement {
    CodeBlock make();
}
