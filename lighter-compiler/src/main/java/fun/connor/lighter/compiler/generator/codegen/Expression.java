package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;

import javax.lang.model.type.TypeMirror;

/**
 * A Java source element that represents a value. This could be a
 * literal, a method call, or a variable. Expressions are also
 * Statements as some expressions have side-effects.
 */
public interface Expression extends TypedElement, Statement {
    CodeBlock makeReadStub();

    @Override
    default CodeBlock make() {
        return makeReadStub();
    }

    /**
     * Create a literal expression
     * @param type The literal type
     * @param value the value the literal should have
     * @param types a {@link LighterTypes} for type utilities
     * @param <T> the type of the literal
     * @return an {@code Expression} representing the Java source literal with the provided value.
     */
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

    /**
     * Create a literal expression with the value {@code null}.
     * @param types a {@link LighterTypes} for type utilities
     * @return an expression with the runtime value null.
     */
    static Expression nullExpr(LighterTypes types) {
        return literal(Void.class, null, types);
    }

    /**
     * Create an expression representing a provided Java source fragment.
     * @param type the expression type
     * @param fragment the Java source code
     * @return the expression representing the provided Java source code
     */
    static Expression code(TypeMirror type, String fragment) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of(fragment);
            }

            @Override
            public TypeMirror getType() {
                return type;
            }
        };
    }
}
