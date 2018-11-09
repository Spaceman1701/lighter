package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

@FunctionalInterface
public interface Statement {
    CodeBlock make();
}
