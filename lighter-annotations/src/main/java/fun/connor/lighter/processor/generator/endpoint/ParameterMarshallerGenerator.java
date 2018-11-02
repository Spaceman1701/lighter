package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.handler.TypeMarshalException;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.Assignable;
import fun.connor.lighter.processor.generator.codegen.Expression;
import fun.connor.lighter.processor.generator.codegen.Statement;
import fun.connor.lighter.processor.generator.codegen.TypeAdaptorGenerator;

public class ParameterMarshallerGenerator implements Statement {

    private Assignable output;
    private Expression inputStr;
    private TypeAdaptorGenerator adaptor;
    private LighterTypes types;

    private boolean exceptionOnNull;

    private ParameterMarshallerGenerator
            (Assignable resultVariable, Expression stringParam,
             TypeAdaptorGenerator adaptor, LighterTypes types, boolean exceptionOnNull) {
        this.output = resultVariable;
        this.inputStr = stringParam;
        this.adaptor = adaptor;
        this.types = types;
        this.exceptionOnNull = exceptionOnNull;
    }

    public static Builder builder(Assignable output, LighterTypes types) {
        return new Builder(output, types);
    }

    @Override
    public CodeBlock make() {

        return CodeBlock.builder()
                .beginControlFlow("if ($L != null)")
                    .addStatement("$L $L", output.makeAssignmentStub(), adaptor.makeDeserialize(inputStr))
                .endControlFlow()
                .beginControlFlow("else")
                    .add(makeNullCaseHandler())
                .endControlFlow()
                .build();
    }

    private CodeBlock makeNullCaseHandler() {
        if (exceptionOnNull) {
            return makeTypeMissingException();
        } else {
            return CodeBlock.builder()
                    .add("$L null", output.makeAssignmentStub())
                    .build();
        }
    }

    private CodeBlock makeTypeMissingException() {
        return CodeBlock.builder()
                .addStatement("throw new $T($S, $S, $T.class)",
                        TypeMarshalException.class, "required type was missing", inputStr.makeReadStub(),
                        adaptor.getAdaptingType())
                .build();
    }


    public static class Builder {
        private Assignable output;
        private Expression inputStr;
        private TypeAdaptorGenerator adaptor;
        private LighterTypes types;

        private boolean exceptionIfNull;

        private Builder(Assignable output, LighterTypes types) {
            this.output = output;
            this.types = types;
        }

        public Builder input(Expression inputStr) {
            this.inputStr = inputStr;
            return this;
        }

        public Builder typeAdaptorGenerator(TypeAdaptorGenerator generator) {
            this.adaptor = generator;
            return this;
        }

        public Builder shouldThrowOnNull(boolean v) {
            this.exceptionIfNull = v;
            return this;
        }

        public ParameterMarshallerGenerator build() {
            return new ParameterMarshallerGenerator(output, inputStr, adaptor, types, exceptionIfNull);
        }

    }
}
