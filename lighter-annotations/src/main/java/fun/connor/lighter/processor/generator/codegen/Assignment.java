package fun.connor.lighter.processor.generator.codegen;

import com.squareup.javapoet.CodeBlock;

public class Assignment implements Statement {

    private Assignable assignable;
    private Readable readable;

    private Assignment(Assignable assignable, Readable readable) {
        this.assignable = assignable;
        this.readable = readable;
    }

    public static Assignment of(Assignable assignable, Readable readable) {
        return new Assignment(assignable, readable);
    }

    @Override
    public CodeBlock make() {
        return CodeBlock.of("$L $L", assignable.makeAssignmentStub(), readable.makeReadStub());
    }
}
