package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.MoreTypes;
import fun.connor.lighter.compiler.generator.codegen.*;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

/**
 * Java source code generator for repeatable logic that marshals method parameters from raw data. This generator
 * and {@link ParameterMarshallerGenerator} are responsible for generating the runtime code that deserializes
 * types for use in endpoint methods. This class is responsible for assigning the Java variables to store the
 * deserialized data (including handling wrapping data in {@link java.util.Optional} when required).
 */
public class MethodParamMarshalGenerator implements Statement {

    private LocalVariable destination;
    private Expression source;
    private Expression contentType;
    private LighterTypes types;
    private TypeAdaptorFactoryGenerator typeAdaptorFactory;

    private final boolean isOptional;


    /**
     * Construct a generator. The generator requires a LocalVariable as a destination for the marshaled type, an
     * expression which produces the data to be marshaled, an expression which produces the IANA Media type to be used
     * for this deserialization, and a generator for the {@link fun.connor.lighter.adapter.TypeAdapterFactory} in order
     * to use it for type deserialization.
     * @param destination the local variable to which the deserialized data will be assigned
     * @param source an expression that produces the serialized data
     * @param contentType an expression which produces a String IANA Media type of the serialized data
     * @param typeAdaptorFactory a generator for a TypeAdapter factory to use for deserialization
     * @param types type utilities
     */
    public MethodParamMarshalGenerator(LocalVariable destination, Expression source, Expression contentType,
                                       TypeAdaptorFactoryGenerator typeAdaptorFactory, LighterTypes types) {
        this.destination = destination;
        this.source = source;
        this.contentType = contentType;
        this.typeAdaptorFactory = typeAdaptorFactory;
        this.types = types;

        isOptional = MoreTypes.isTypeOptional(destination.getType());
    }


    /**
     * Make a block of code that marshals the source expression into the destination variable. The code block
     * generated will be multiple lines of Java source code. This method will generate local variables which are
     * not exposed otherwise. It attempts to avoid possible collisions between its internal variables and the
     * variables of other generators by applying slight name mangling.
     * @return the statement as a JavaPoet {@link CodeBlock}
     */
    @Override
    public CodeBlock make() {

        LocalVariable tempStr =
                new LocalVariable(types.mirrorOfClass(String.class), destination.getName() + "_Str");

        CodeBlock.Builder builder = CodeBlock.builder();

        builder.add(tempStr.makeDeclaration());
        builder.addStatement(Assignment.of(tempStr, source).make());

        if (isOptional) {
            builder.add(buildOptionalBlock(tempStr));
        } else {
            builder.add(buildMarshalBlock(destination, tempStr, true));
        }

        return builder.build();
    }

    /**
     * Generate code for optional types. This methods simply adds a few wrapping lines of code that create a
     * temporary nullable variable.
     * @param tempStr the local variable containing the serialized data
     * @return the code block
     */
    private CodeBlock buildOptionalBlock(LocalVariable tempStr) {
        //TODO: deal with primitives
        TypeMirror optionType = types.extractOptionalType((DeclaredType) destination.getType());
        LocalVariable nullableVar = new LocalVariable(optionType, destination.getName() + "_Nullable");

        CodeBlock marshalBlock = buildMarshalBlock(nullableVar, tempStr, false);

        OptionalGenerator optionalGenerator = new OptionalGenerator(nullableVar, types);

        return CodeBlock.builder()
                .add(nullableVar.makeDeclaration())
                .add(marshalBlock)
                .addStatement(Assignment.of(destination, optionalGenerator).make())
                .build();
    }

    private CodeBlock buildMarshalBlock(Assignable destination, Expression input, boolean throwOnNull) {
        TypeAdaptorGenerator typeAdaptorGenerator = typeAdaptorFactory.makeGetTypeAdaptor(destination.getType(), contentType);
        return ParameterMarshallerGenerator.builder(destination, types)
                .shouldThrowOnNull(throwOnNull)
                .typeAdaptorGenerator(typeAdaptorGenerator)
                .input(input)
                .build().make();
    }
}
