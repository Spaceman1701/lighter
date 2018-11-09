package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.type.TypeMirror;

public class LocalVariable implements Assignable, Expression {
    private TypeMirror type;
    private String name;

    public LocalVariable(TypeMirror type, String name) {
        this.type = type;
        this.name = name;
    }

    public CodeBlock makeDeclaration() {
        return CodeBlock.builder()
                .addStatement("$T $L", type, name)
                .build();
    }

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
