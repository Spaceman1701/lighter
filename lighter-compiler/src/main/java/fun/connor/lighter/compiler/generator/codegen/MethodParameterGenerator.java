package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * A generator for variables that are declared as method parameters. This class is similar
 * to the {@link LocalVariable} class, except that method parameters are declared in the parameter
 * lists of methods. Method parameters are always marked as {@code final} and cannot be assigned to.
 */
public class MethodParameterGenerator implements Expression {

    private TypeMirror type;
    private String name;

    /**
     * Create a method parameter of the given type and name
     * @param type the parameter type
     * @param name the parameter name
     */
    public MethodParameterGenerator(TypeMirror type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Builds a {@link ParameterSpec} for this parameter
     * @return this parameter as a Java poet {@link ParameterSpec}.
     */
    public ParameterSpec getParameterSpec() {
        return ParameterSpec.builder(TypeName.get(type), name, Modifier.FINAL)
                .build();
    }

    @Override
    public CodeBlock makeReadStub() {
        return CodeBlock.of("$L", name);
    }

    @Override
    public TypeMirror getType() {
        return type;
    }
}
