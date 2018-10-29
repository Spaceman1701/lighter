package fun.connor.lighter.processor.generator.endpoint;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.type.TypeMirror;

public abstract class ParamBlockGenerator {

    protected final String parameterNameInMap;
    protected final String parameterMapName;
    protected final TypeMirror expectedType;
    protected final String parameterName;

    public ParamBlockGenerator
            (String parameterNameInMap, String parameterMapName, TypeMirror expectedType, String parameterName) {
        this.parameterNameInMap = parameterNameInMap;
        this.parameterMapName = parameterMapName;
        this.expectedType = expectedType;
        this.parameterName = parameterName;
    }

    public abstract CodeBlock build();
}
