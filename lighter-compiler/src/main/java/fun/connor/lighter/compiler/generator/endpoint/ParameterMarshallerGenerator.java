package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.generator.codegen.*;
import fun.connor.lighter.handler.TypeMarshalException;

import static java.util.Objects.requireNonNull;

/**
 * Generator for the logic that deserializes data into java classes and handles marshalling errors.
 */
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


    /**
     * Fluent builder for {@link ParameterMarshallerGenerator}.
     */
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

        /**
         * Set the expression that produces the serialized data
         * @param inputStr the expression for the serialized data
         * @return self
         */
        public Builder input(Expression inputStr) {
            this.inputStr = inputStr;
            return this;
        }

        /**
         * Set the generator used for generating code for {@link fun.connor.lighter.adapter.TypeAdapter}s.
         * @param generator the generator
         * @return self
         */
        public Builder typeAdaptorGenerator(TypeAdaptorGenerator generator) {
            this.adaptor = generator;
            return this;
        }

        /**
         * Set flag that defines whether to throw a {@link TypeMarshalException} if the serialized data is {@code null}.
         * For required types, this flag is usually set to {@code true}. For optional values, this flag is normally
         * {@code false}.
         * @param v the flag value
         * @return self
         */
        public Builder shouldThrowOnNull(boolean v) {
            this.exceptionIfNull = v;
            return this;
        }

        /**
         * Create a {@link ParameterMarshallerGenerator} from this Builder's configuration
         * @return a new generator instance
         */
        public ParameterMarshallerGenerator build() {
            return new ParameterMarshallerGenerator(output, inputStr, adaptor, types, exceptionIfNull);
        }

    }
}
