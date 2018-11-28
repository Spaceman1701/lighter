package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.FieldSpec;

/**
 * Java source code element representing a class field.
 */
public interface Field {

    /**
     * @return the field's specification.
     */
    FieldSpec getField();
}
