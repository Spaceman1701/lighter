package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.processor.LighterTypes;

import javax.lang.model.type.TypeMirror;

public interface Expression extends TypedElement, Statement {
    CodeBlock makeReadStub();

    @Override
    default CodeBlock make() {
        return makeReadStub();
    }

    static <T> Expression literal(Class<T> type, T value, LighterTypes types) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                if (type.equals(String.class)) { //String needs escaping quotes
                    return CodeBlock.of("$S", value);
                } else {
                    return CodeBlock.of("$L", value);
                }
            }

            @Override
            public TypeMirror getType() {
                return types.mirrorOfClass(type);
            }
        };
    }

    static Expression nullExpr(LighterTypes types) {
        return literal(Void.class, null, types);
    }
}
