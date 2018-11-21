package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.Expression;

import javax.lang.model.type.TypeMirror;

public class TypeAdaptorGenerator implements Expression {

    private Expression source;
    private TypeMirror producedType;
    private LighterTypes types;

    TypeAdaptorGenerator(Expression source, TypeMirror producedType, LighterTypes types) {
        this.source = source;
        this.types = types;
        this.producedType = producedType;
    }

    public TypeMirror getAdaptingType() {
        return producedType;
    }

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
