package fun.connor.lighter.compiler.generator.codegen;

import com.squareup.javapoet.CodeBlock;

/**
 * A Java source code statement that represents the assignment of
 * an {@link Expression} to an {@link Assignable}.
 */
public class Assignment implements Statement {

    private Assignable assignable;
    private Expression expression;

    private Assignment(Assignable assignable, Expression expression) {
        this.assignable = assignable;
        this.expression = expression;
    }

    /**
     * Named constructor that creates an assignment from {@code expression} to
     * {@code assignable}.
     * @param assignable the assignable
     * @param expression the expression
     * @return a {@link Statement} that represents the assignment of the two elements.
     */
    public static Assignment of(Assignable assignable, Expression expression) {
        return new Assignment(assignable, expression);
    }

    @Override
    public CodeBlock make() {
        return CodeBlock.of("$L = $L", assignable.makeAssignmentStub(), expression.makeReadStub());
    }
}
