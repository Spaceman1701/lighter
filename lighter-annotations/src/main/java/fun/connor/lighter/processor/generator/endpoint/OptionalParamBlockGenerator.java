package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

public class OptionalParamBlockGenerator extends ParamBlockGenerator {

    public OptionalParamBlockGenerator
            (String parameterNameInMap, String parameterMapName, TypeName expectedType, String parameterName) {
        super(parameterNameInMap, parameterMapName, expectedType, parameterName);
    }

    @Override
    public CodeBlock build() {
        String parameterStrName = parameterName + "Str";
        return CodeBlock.builder()
                .addStatement("String $L = $L.get($S)", parameterStrName, parameterMapName, parameterNameInMap)
                .addStatement("$T $L", expectedType, parameterName)
                .beginControlFlow("if ($L == null)", parameterStrName)
                    .addStatement("$L = null", parameterName)
                .endControlFlow()
                .beginControlFlow("else")
                    .addStatement("$L = typeMarshaller.marshal($L, $T.class)",
                            parameterName, parameterStrName, expectedType)
                .endControlFlow()
                .build();
    }
}
