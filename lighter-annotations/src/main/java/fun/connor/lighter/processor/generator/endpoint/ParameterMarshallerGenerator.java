package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.handler.TypeMarshalException;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.codegen.Assignable;
import fun.connor.lighter.processor.generator.codegen.Assignment;
import fun.connor.lighter.processor.generator.codegen.Expression;
import fun.connor.lighter.processor.generator.codegen.Statement;

import static java.util.Objects.requireNonNull;

public class ParameterMarshallerGenerator implements Statement {

    private Assignable output;
    private Expression inputStr;
    private TypeAdaptorGenerator adaptor;
    private LighterTypes types;

    private boolean exceptionOnNull;

    private ParameterMarshallerGenerator
            (Assignable resultVariable, Expression stringParam,
             TypeAdaptorGenerator adaptor, LighterTypes types, boolean exceptionOnNull) {
        this.output = requireNonNull(resultVariable);
        this.inputStr = requireNonNull(stringParam);
        this.adaptor = requireNonNull(adaptor, makeTypeAdaptorMissingMsg());
        this.types = requireNonNull(types);
        this.exceptionOnNull = exceptionOnNull;
    }

    private String makeTypeAdaptorMissingMsg() {
        return "No possible type adaptor for output type " + output.getType().toString() + "\n"
                + "The name of the parameter that caused the error was: " + output.toString();
    }

    public static Builder builder(Assignable output, LighterTypes types) {
        return new Builder(output, types);
    }

    @Override
    public CodeBlock make() {

        return CodeBlock.builder()
                .beginControlFlow("if ($L != null)", inputStr.makeReadStub())
                    .addStatement(Assignment.of(output, adaptor.makeDeserialize(inputStr)).make())
                .endControlFlow()
                .beginControlFlow("else")
                    .addStatement(makeNullCaseHandler())
                .endControlFlow()
                .build();
    }

    private CodeBlock makeNullCaseHandler() {
        if (exceptionOnNull) {
            return makeTypeMissingException();
        } else {
            return Assignment.of(output, Expression.nullExpr(types)).make();
        }
    }

    private CodeBlock makeTypeMissingException() {
        return CodeBlock.builder()
                .add("throw new $T($S, $S, $T.class)",
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
