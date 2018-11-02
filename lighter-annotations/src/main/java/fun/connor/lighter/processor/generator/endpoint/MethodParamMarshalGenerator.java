package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.MoreTypes;
import fun.connor.lighter.processor.generator.codegen.*;
import fun.connor.lighter.processor.generator.codegen.Expression;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

public class MethodParamMarshalGenerator implements Statement {

    private LocalVariable destination;
    private Expression source;
    private LighterTypes types;
    private Map<TypeName, TypeAdaptorGenerator> generatorMap;

    private boolean isOptional;

    public MethodParamMarshalGenerator
            (LocalVariable destination, Expression source, Map<TypeName, TypeAdaptorGenerator> generatorMap,
             LighterTypes types) {
        this.destination = destination;
        this.source = source;
        this.types = types;
        this.generatorMap = generatorMap;

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

    private TypeAdaptorGenerator getTypeAdaptor(TypeMirror type) {
        TypeMirror erasedType = types.erasure(type);
        for (TypeName typeName : generatorMap.keySet()) {
            System.out.println(typeName + " is in map");
        }
        System.out.println(TypeName.get(erasedType) + " is required");
        return generatorMap.get(TypeName.get(erasedType));
    }

    private CodeBlock buildOptionalBlock(LocalVariable tempStr) {
        //TODO: deal with primitives
        TypeMirror optionType = types.extractOptionalType((DeclaredType) destination.getType());
        LocalVariable nullableVar = new LocalVariable(optionType, destination.getName() + "_Nullable");

        CodeBlock marshalBlock = buildMarshalBlock(nullableVar, tempStr, false);

        OptionalGenerator optionalGenerator = new OptionalGenerator(nullableVar, types);

        return marshalBlock.toBuilder()
                .addStatement(Assignment.of(destination, optionalGenerator).make())
                .build();
    }

    private CodeBlock buildMarshalBlock(Assignable destination, Expression input, boolean throwOnNull) {
        return ParameterMarshallerGenerator.builder(destination, types)
                .shouldThrowOnNull(throwOnNull)
                .typeAdaptorGenerator(getTypeAdaptor(destination.getType()))
                .input(input)
                .build().make();
    }
}
