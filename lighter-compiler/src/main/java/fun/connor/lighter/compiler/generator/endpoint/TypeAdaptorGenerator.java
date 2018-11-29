package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.Expression;

import javax.lang.model.type.TypeMirror;

/**
 * Java source code generator for {@link fun.connor.lighter.adapter.TypeAdapter}. This generator can
 * represent instances from any source. The source expression will be duplicated for each piece of
 * code generated using this generator.
 */
public class TypeAdaptorGenerator implements Expression {

    private Expression source;
    private TypeMirror producedType;
    private LighterTypes types;

    /**
     * Construct a new TypeAdaptorGenerator
     * @param source an expression which produces a {@link fun.connor.lighter.adapter.TypeAdapter} at runtime
     * @param producedType the type that the generated TypeAdapter produces
     * @param types type utilities
     */
    TypeAdaptorGenerator(Expression source, TypeMirror producedType, LighterTypes types) {
        this.source = source;
        this.types = types;
        this.producedType = producedType;
    }

    /**
     * Get the type produced by the generated {@link fun.connor.lighter.adapter.TypeAdapter}.
     * @return the type as a{@link TypeMirror}
     */
    public TypeMirror getAdaptingType() {
        return producedType;
    }

    /**
     * Generate an expression which represents a method call to
     * {@link fun.connor.lighter.adapter.TypeAdapter#serialize(Object)}.
     * @param object an expression which produces the runtime argument for the method
     * @return an expression representing the method call with the given argument
     */
    public Expression makeSerialize(Expression object) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.serialize($L)", source.makeReadStub(), object.makeReadStub());
            }

            @Override
            public TypeMirror getType() {
                return types.mirrorOfClass(String.class);
            }
        };
    }

    /**
     * Generate an expression which represents a method call to
     * {@link fun.connor.lighter.adapter.TypeAdapter#deserialize(String)}.
     * @param string an expression which produces the runtime argument for the method
     * @return an expression representing the method call with the given argument
     */
    public Expression makeDeserialize(Expression string) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.deserialize($L)", source.makeReadStub(), string.makeReadStub());
            }

            @Override
            public TypeMirror getType() {
                return producedType;
            }
        };
    }

    @Override
    public CodeBlock makeReadStub() {
        return source.makeReadStub();
    }

    @Override
    public TypeMirror getType() {
        return source.getType();
    }
}
