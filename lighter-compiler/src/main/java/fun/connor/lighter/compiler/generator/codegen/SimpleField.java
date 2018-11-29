package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Implementation of {@link Field} which can represent a mutable class field. This field
 * is both an {@link Assignable} and an {@link Expression}.
 */
public class SimpleField implements Field, Expression, Assignable {

    private FieldSpec field;
    private TypeMirror type;

    /**
     * Construct a field with a given type and name
     * @param type the field type
     * @param name the field name
     */
    public SimpleField(TypeMirror type, String name) {
        this.type = type;

        field = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE).build();
    }

    /**
     * Get the field name
     * @return the name of the field as a String
     */
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
