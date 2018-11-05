package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.Expression;

import javax.lang.model.type.TypeMirror;

public class MapGenerator {

    private TypeMirror keyType;
    private TypeMirror valueType;
    private Expression source;

    private LighterTypes types;

    public MapGenerator(TypeMirror keyType, TypeMirror valueType, Expression source, LighterTypes types) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.types = types;
        this.source = source;
    }

    public MapGenerator(Class keyType, Class valueType, Expression source, LighterTypes types) {
        this(types.mirrorOfClass(keyType), types.mirrorOfClass(valueType), source, types);
    }

    public Expression makeGet(Expression keyExpr) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.get($L)", source.makeReadStub(), keyExpr.makeReadStub());
            }

            @Override
            public TypeMirror getType() {
                return valueType;
            }
        };
    }

    public Expression makeExpression() {
        return source;
    }
}
