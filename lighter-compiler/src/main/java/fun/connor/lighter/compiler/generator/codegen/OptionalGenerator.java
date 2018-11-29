package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;

import javax.lang.model.type.TypeMirror;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Generator for {@link java.util.Optional} and related types. This generator can used
 * philosophically to represent all of the Java standard library optional types.
 */
public class OptionalGenerator implements Expression {

    private Expression source;
    private LighterTypes types;

    /**
     * Construct an OptionalGenerator from a source expression which has a runtime value of an {@link Optional}
     * or related type.
     * @param source expression that produces Optional
     * @param types type utilities
     */
    public OptionalGenerator(Expression source, LighterTypes types) {
        this.types = types;
        this.source = source;
    }


    @Override
    public CodeBlock makeReadStub() {
        //TODO: handle primitive types
        return CodeBlock.of("$T.ofNullable($L)", Optional.class, source.makeReadStub());
    }

    @Override
    public TypeMirror getType() {
        switch (source.getType().getKind()) {
            case INT:
                return types.mirrorOfClass(OptionalInt.class);
            case LONG:
                return types.mirrorOfClass(OptionalLong.class);
            case DOUBLE:
                return types.mirrorOfClass(OptionalDouble.class);
            default:
                return types.mirrorOfClass(Optional.class);
        }
    }
}
