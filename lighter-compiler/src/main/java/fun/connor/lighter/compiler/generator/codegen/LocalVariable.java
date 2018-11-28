package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.type.TypeMirror;

/**
 * A Java source code element that represents a local method variable. Represents a
 * mutable reference which is both an {@link Expression} with a value and an {@link Assignable}
 * reference.
 */
public class LocalVariable implements Assignable, Expression {
    private TypeMirror type;
    private String name;

    /**
     * Create a local variable of the give type and the given name. The provided name
     * will appear in generated Java source code.
     * @param type the variable type
     * @param name the variable name
     */
    public LocalVariable(TypeMirror type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Make a statement that represents the variable's declaration.
     * Example: {@code String foo;}
     * @return a statement declaring the variable.
     */
    public CodeBlock makeDeclaration() {
        return CodeBlock.builder()
                .addStatement("$T $L", type, name)
                .build();
    }

    /**
     * @return the variable's name
     */
    public String getName() {
        return name;
    }

    @Override
    public CodeBlock makeAssignmentStub() {
        return CodeBlock.of("$L", name);
    }

    @Override
    public CodeBlock makeReadStub() {
        return CodeBlock.of("$L", name);
    }

    @Override
    public TypeMirror getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LocalVariable: " + name + "";
    }
}
