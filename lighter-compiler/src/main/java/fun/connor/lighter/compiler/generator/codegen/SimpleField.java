package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class SimpleField implements Field, Expression, Assignable {

    private FieldSpec field;
    private TypeMirror type;

    public SimpleField(TypeMirror type, String name) {
        this.type = type;

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE).build();
    }

    public String getName() {
        return field.name;
    }

    @Override
    public FieldSpec getField() {
        return field;
    }

    @Override
    public CodeBlock makeAssignmentStub() {
        return CodeBlock.of("this.$N", field);
    }

    @Override
    public CodeBlock makeReadStub() {
        return CodeBlock.of("this.$N", field);
    }

    @Override
    public TypeMirror getType() {
        return type;
    }
}
