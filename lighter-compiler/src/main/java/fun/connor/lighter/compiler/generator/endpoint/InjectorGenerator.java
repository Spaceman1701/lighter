package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.generator.codegen.Expression;

import javax.lang.model.type.TypeMirror;

/**
 * Java source code generator for the {@link fun.connor.lighter.injection.InjectionObjectFactory}.
 */
public class InjectorGenerator {

    private Expression source;

    /**
     * Construct an InjectorGenerator from an expression which has the runtime value of a
     * {@link fun.connor.lighter.injection.InjectionObjectFactory}.
     * @param source an expression which produces an {@link fun.connor.lighter.injection.InjectionObjectFactory}
     */
    public InjectorGenerator(Expression source) {
        this.source = source;
    }

    /**
     * Generate an expression which represents a call to
     * {@link fun.connor.lighter.injection.InjectionObjectFactory#newInstance(Class)}
     * @param type the type to use as the argument to {@code newInstance}.
     * @return an expression representing the method call with the provided argument.
     */
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
