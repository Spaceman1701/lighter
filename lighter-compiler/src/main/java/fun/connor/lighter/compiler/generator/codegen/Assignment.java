package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

public class Assignment implements Statement {

    private Assignable assignable;
    private Expression expression;

    private Assignment(Assignable assignable, Expression expression) {
        this.assignable = assignable;
        this.expression = expression;
    }

    public static Assignment of(Assignable assignable, Expression expression) {
        return new Assignment(assignable, expression);
    }

    @Override
    public CodeBlock make() {
        return CodeBlock.of("$L = $L", assignable.makeAssignmentStub(), expression.makeReadStub());
    }
}
