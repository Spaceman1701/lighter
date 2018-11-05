package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.processor.generator.codegen.Expression;

import javax.lang.model.type.TypeMirror;

public class InjectorGenerator {

    private Expression source;

    public InjectorGenerator(Expression source) {
        this.source = source;
    }

    public Expression newInstance(TypeMirror type) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.newInstance($T.class)", source.makeReadStub(), type);
            }

            @Override
            public TypeMirror getType() {
                return type;
            }
        };
    }
}
