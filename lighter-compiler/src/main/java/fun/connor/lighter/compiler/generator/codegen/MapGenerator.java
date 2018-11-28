package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;

import javax.lang.model.type.TypeMirror;

/**
 * Code generator for {@link java.util.Map} objects. This object mirrors the interface
 * of Map but instead provides methods for generating usages of Map's interface. This allows
 * changes in Map's interface to be encapsulated.
 */
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

    /**
     * Generator method for {@link java.util.Map#get(Object)}
     * @param keyExpr expression that produces the map key
     * @return an expression that represents the method call.
     */
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

    /**
     * Generator method for {@link java.util.Map#put(Object, Object)}
     * @param keyExpr expression that produces the map key
     * @param valueExpr expression that produces the map value
     * @return expression that represents the method call
     */
    public Expression makePut(Expression keyExpr, Expression valueExpr) {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.put($L, $L)", source.makeReadStub(), keyExpr.makeReadStub(), valueExpr.makeReadStub());
            }

            @Override
            public TypeMirror getType() {
                return valueExpr.getType();
            }
        };
    }

    public Expression makeExpression() {
        return source;
    }
}
