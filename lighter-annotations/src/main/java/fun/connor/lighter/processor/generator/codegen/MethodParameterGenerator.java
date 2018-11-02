package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class MethodParameterGenerator implements Expression {

    private TypeMirror type;
    private String name;

    public MethodParameterGenerator(TypeMirror type, String name) {
        this.type = type;
        System.out.println("creating method param with " + TypeName.get(type));
        this.name = name;
    }

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
