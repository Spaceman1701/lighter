package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.Assignable;
import fun.connor.lighter.compiler.generator.codegen.Expression;
import fun.connor.lighter.compiler.generator.codegen.Field;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Java source code generator for using {@link TypeAdapterFactory}. This generator represents TypeAdapterFactories
 * that are stored in class fields.
 */
public class TypeAdaptorFactoryGenerator implements Field {

    private FieldSpec field;
    private TypeMirror type;
    private LighterTypes types;


    /**
     * Construct a new TypeAdapterFactoryGenerator
     * @param name the name of the field
     * @param types type utilities
     */
    public TypeAdaptorFactoryGenerator(String name, LighterTypes types) {
        this.types = types;

        this.type = types.mirrorOfClass(TypeAdapterFactory.class);

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    /**
     * Get the field specification for that this generator controls
     * @return the JavaPoet {@link FieldSpec}.
     */
    @Override
    public FieldSpec getField() {
        return field;
    }

    /**
     * Get the type of the field that this generator controls
     * @return the type as {@link TypeMirror}
     */
    public TypeMirror getType() {
        return type;
    }

    /**
     * Generates a {@link TypeAdaptorGenerator} which represents a {@link TypeAdapter} that can serialize and
     * deserialize the provided type and IANA media type
     * @param clazz type which TypeAdapter should provide
     * @param mediaType IANA media type TypeAdapter should provide
     * @return A java source code generator that can generate code that uses {@link TypeAdapter}.
     */
    public TypeAdaptorGenerator makeGetTypeAdaptor(TypeMirror clazz, Expression mediaType) {
        CodeBlock access = makeExpression().makeReadStub();
        Expression adaptorSource = new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("$L.getAdapter($T.class, $L)", access, clazz, mediaType.makeReadStub());
            }

            @Override
            public TypeMirror getType() {
                return types.mirrorOfParameterizedClass(TypeAdapter.class, (DeclaredType) clazz);
            }
        };
        return new TypeAdaptorGenerator(adaptorSource, clazz, types);
    }


    /**
     * Create an expression that produces the value of the field
     * @return the field as an {@link Expression}
     */
    public Expression makeExpression() {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("this.$N", field);
            }

            @Override
            public TypeMirror getType() {
                return type;
            }
        };
    }

    /**
     * Create an assignable for the field
     * @return the field as an {@link Assignable}
     */
    public Assignable makeAssignable() {
        return new Assignable() {
            @Override
            public CodeBlock makeAssignmentStub() {
                return CodeBlock.of("this.$N", field);
            }

            @Override
            public TypeMirror getType() {
                return type;
            }
        };
    }
}
