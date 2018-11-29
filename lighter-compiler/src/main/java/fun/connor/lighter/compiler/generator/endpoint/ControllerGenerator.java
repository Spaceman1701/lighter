package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.compiler.generator.codegen.Assignable;
import fun.connor.lighter.compiler.generator.codegen.Expression;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generator for {@link fun.connor.lighter.declarative.ResourceController} annotated classes. Since these
 * classes are not required to conform to any interface, this generator uses extra data to generate
 * endpoint method calls. This allows the class to represent any {@link fun.connor.lighter.declarative.ResourceController}
 * and any endpoint method call with the same interface.
 */
public class ControllerGenerator {

    private String fieldName;
    private TypeMirror fieldType;
    private FieldSpec fieldSpec;

    private String endpointMethodName;
    private TypeMirror endpointRetType;

    /**
     * Construct a new controller generator for the given {@link fun.connor.lighter.declarative.ResourceController}
     * annotated type. Also required is a name for the field to store the controller at runtime, the name of
     * the endpoint method that this generator will be generating, and that endpoint's return type.
     * @param type the controller's type
     * @param name the name of the field this generator generates
     * @param endpointMethodName the name of the endpoint method to call
     * @param endpointRetType the return type of the endpoint.
     */
    public ControllerGenerator(TypeMirror type, String name, String endpointMethodName, TypeMirror endpointRetType) {
        this.fieldName = name;
        this.fieldType = type;
        this.endpointMethodName = endpointMethodName;
        this.endpointRetType = endpointRetType;


        fieldSpec = FieldSpec.builder(TypeName.get(type), name, Modifier.PRIVATE).build();
    }


    /**
     * Build a JavaPoet {@link FieldSpec} for this controller
     * @return the FieldSpec
     */
    public FieldSpec getField() {
        return fieldSpec;
    }

    /**
     * Get the controller type
     * @return the type of the the controller
     */
    public TypeMirror getType() {return fieldType;}

    /**
     * Make an {@link Assignable} element to assign to the field this generator controls
     * @return an {@link Assignable} for the field
     */
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

    /**
     * Make an expression to read the field.
     * @return an expression with the value of the field
     */
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

    /**
     * Generate an expression that represents a function call to the endpoint method on the field. The
     * argument list is provided in the order used in the final generated source code. Each expression in the
     * list should produce the value of the argument in its position.
     * @param arguments the ordered argument list
     * @return an expression representing the method call with the provided arguments.
     */
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
