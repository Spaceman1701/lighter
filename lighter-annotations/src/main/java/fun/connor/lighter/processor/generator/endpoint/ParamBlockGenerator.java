package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

public abstract class ParamBlockGenerator {

    protected final String parameterNameInMap;
    protected final String parameterMapName;
    protected final TypeName expectedType;
    protected final String parameterName;

    public ParamBlockGenerator
            (String parameterNameInMap, String parameterMapName, TypeName expectedType, String parameterName) {
        this.parameterNameInMap = parameterNameInMap;
        this.parameterMapName = parameterMapName;
        this.expectedType = expectedType;
        this.parameterName = parameterName;
    }

    public abstract CodeBlock build();
}
