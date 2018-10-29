package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import fun.connor.lighter.handler.TypeMarshalException;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class RequiredParamBlockGenerator extends ParamBlockGenerator {

    public RequiredParamBlockGenerator
            (String parameterNameInMap, String parameterMapName, TypeMirror expectedType, String parameterName) {
        super(parameterNameInMap, parameterMapName, expectedType, parameterName);
    }

    @Override
    public CodeBlock build() {
        String parameterStrName = parameterName + "_Str";
        return CodeBlock.builder()
                .addStatement("String $L = $L.get($S)", parameterStrName, parameterMapName, parameterNameInMap)
                .beginControlFlow("if ($L == null)", parameterStrName)
                    .addStatement("throw new $T($S, $S, $T.class)", TypeMarshalException.class, "bad", "bad", expectedType)
                .endControlFlow()
                .addStatement("$T $L = typeMarshaller.marshal($L, $T.class)", expectedType,
                        parameterName, parameterStrName, expectedType)
                .build();
    }
}
