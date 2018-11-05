package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.MoreTypes;
import fun.connor.lighter.processor.generator.codegen.Assignable;
import fun.connor.lighter.processor.generator.codegen.Expression;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class TypeAdaptorFactoryGenerator {

    private FieldSpec field;
    private TypeMirror type;
    private LighterTypes types;


    public TypeAdaptorFactoryGenerator(String name, LighterTypes types) {
        this.types = types;

        this.type = types.mirrorOfClass(TypeAdapterFactory.class);

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    public FieldSpec getField() {
        return field;
    }

    public TypeMirror getType() {
        return type;
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
