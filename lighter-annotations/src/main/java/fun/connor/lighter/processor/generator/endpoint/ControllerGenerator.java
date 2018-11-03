package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.processor.generator.codegen.Assignable;
import fun.connor.lighter.processor.generator.codegen.Expression;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerGenerator {

    private String fieldName;
    private TypeMirror fieldType;
    private FieldSpec fieldSpec;

    private String endpointMethodName;
    private TypeMirror endpointRetType;

    public ControllerGenerator(TypeMirror type, String name, String endpointMethodName, TypeMirror endpointRetType) {
        this.fieldName = name;
        this.fieldType = type;
        this.endpointMethodName = endpointMethodName;
        this.endpointRetType = endpointRetType;


        fieldSpec = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE).build();
    }


    public FieldSpec getField() {
        return fieldSpec;
    }

    public TypeMirror getType() {return fieldType;}

    public Assignable makeAssignable() {
        return new Assignable() {
            @Override
            public CodeBlock makeAssignmentStub() {
                return CodeBlock.of("this.$N", fieldSpec);
            }

            @Override
            public TypeMirror getType() {
                return fieldType;
            }
        };
    }

    public Expression makeExpression() {
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                return CodeBlock.of("this.$N", fieldSpec);
            }

            @Override
            public TypeMirror getType() {
                return fieldType;
            }
        };
    }

    public Expression makeEndpointCall(List<Expression> arguments) {
        List<CodeBlock> argumentCode = arguments.stream()
                .map(Expression::make)
                .collect(Collectors.toList());
        CodeBlock argumentsList = CodeBlock.join(argumentCode, ",");
        return new Expression() {
            @Override
            public CodeBlock makeReadStub() {
                Expression expr = makeExpression();
                return CodeBlock.of("$L.$L($L)", expr.makeReadStub(), endpointMethodName, argumentsList);
            }

            @Override
            public TypeMirror getType() {
                return endpointRetType;
            }
        };
    }
}
