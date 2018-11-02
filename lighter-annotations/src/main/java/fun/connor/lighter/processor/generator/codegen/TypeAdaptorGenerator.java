package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.*;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.processor.LighterTypes;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

public class TypeAdaptorGenerator {

    private TypeMirror adaptingType;
    private DeclaredType fieldType;
    private FieldSpec field;

    private LighterTypes types;

    public TypeAdaptorGenerator(TypeMirror adaptingType, LighterTypes types) {
        this.types = types;
        this.adaptingType = types.erasure(adaptingType);
        if (adaptingType.getKind().isPrimitive()) {
            this.adaptingType = types.boxedClass((PrimitiveType) adaptingType).asType();
        }

        DeclaredType typeAdaptorMirror = (DeclaredType) types.mirrorOfClass(TypeAdapter.class);

        this.fieldType = types.getDeclaredType((TypeElement)typeAdaptorMirror.asElement(), this.adaptingType);

        TypeName typeName =
                ParameterizedTypeName.get(fieldType);

        String fieldName = TypeName.get(this.adaptingType).toString().replace('.', '_') + "Adapter";

        field = FieldSpec.builder(typeName, fieldName, Modifier.PRIVATE, Modifier.FINAL).build();
    }

    public FieldSpec getField() {
        return field;
    }

    public TypeMirror getAdaptingType() {
        return adaptingType;
    }

    public Assignable makeAssignable() {
        return new Assignable() {
            @Override
            public CodeBlock makeAssignmentStub() {
                return CodeBlock.of("$N", field);
            }

            @Override
            public TypeMirror getType() {
                return fieldType;
            }
        };
    }

    public Expression makeDeserialize(Expression readFrom) {
        CodeBlock fromStub = readFrom.makeReadStub();

        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.builder()
                        .add("$N.deserialize($L)", field, fromStub)
                        .build();
            }

            @Override
            public TypeMirror getType() {
                return adaptingType;
            }
        };
    }

    public Expression makeSerialize(Expression readFrom) {
        CodeBlock readStub = readFrom.makeReadStub();

        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.builder()
                        .add("$N.deserialize($L)", field, readStub)
                        .build();
            }

            @Override
            public TypeMirror getType() {
                return types.mirrorOfClass(String.class);
            }
        };
    }


}
