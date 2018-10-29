package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

public class OptionalParamBlockGenerator extends ParamBlockGenerator {

    public OptionalParamBlockGenerator
            (String parameterNameInMap, String parameterMapName, TypeMirror expectedType, String parameterName) {
        super(parameterNameInMap, parameterMapName, expectedType, parameterName);
    }

    private TypeName extractOptionalType() {
        DeclaredType declaredType = (DeclaredType) expectedType;
        return TypeName.get(declaredType.getTypeArguments().get(0));
    }

    @Override
    public CodeBlock build() {
        String parameterStrName = parameterName + "_Str";
        String paramOptionName = parameterName + "_Optional";
        TypeName optionalGenericType = extractOptionalType();
        return CodeBlock.builder()
                .addStatement("String $L = $L.get($S)", parameterStrName, parameterMapName, parameterNameInMap)
                .addStatement("$T $L", optionalGenericType, paramOptionName)
                .beginControlFlow("if ($L == null)", parameterStrName)
                    .addStatement("$L = null", paramOptionName)
                .endControlFlow()
                .beginControlFlow("else")
                    .addStatement("$L = typeMarshaller.marshal($L, $T.class)",
                            paramOptionName, parameterStrName, optionalGenericType)
                .endControlFlow()
                .addStatement("$T<$T> $L = $T.ofNullable($L)", Optional.class, optionalGenericType, parameterName,
                        Optional.class, paramOptionName)
                .build();
    }
}
