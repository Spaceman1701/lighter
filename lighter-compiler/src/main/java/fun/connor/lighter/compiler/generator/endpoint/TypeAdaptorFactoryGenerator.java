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

public class TypeAdaptorFactoryGenerator implements Field {

    private FieldSpec field;
    private TypeMirror type;
    private LighterTypes types;


    public TypeAdaptorFactoryGenerator(String name, LighterTypes types) {
        this.types = types;

        this.type = types.mirrorOfClass(TypeAdapterFactory.class);

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    @Override
    public FieldSpec getField() {
        return field;
    }

    public TypeMirror getType() {
        return type;
    }

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
