package fun.connor.lighter.compiler.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.compiler.LighterTypes;
import fun.connor.lighter.compiler.MoreTypes;
import fun.connor.lighter.compiler.generator.codegen.*;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

public class MethodParamMarshalGenerator implements Statement {

    private LocalVariable destination;
    private Expression source;
    private Expression contentType;
    private LighterTypes types;
    private TypeAdaptorFactoryGenerator typeAdaptorFactory;

    private final boolean isOptional;


    public MethodParamMarshalGenerator(LocalVariable destination, Expression source, Expression contentType,
                                       TypeAdaptorFactoryGenerator typeAdaptorFactory, LighterTypes types) {
        this.destination = destination;
        this.source = source;
        this.contentType = contentType;
        this.typeAdaptorFactory = typeAdaptorFactory;
        this.types = types;

        isOptional = MoreTypes.isTypeOptional(destination.getType());
    }


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
