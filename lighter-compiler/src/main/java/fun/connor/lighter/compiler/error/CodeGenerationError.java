package fun.connor.lighter.compiler.error;

import javax.lang.model.element.Element;

public class CodeGenerationError extends AbstractCompilerError {

    private Element target;
    private String message;

    public CodeGenerationError(Element target, String message) {
        this.target = target;
        this.message = message;
    }

    @Override
    public String toString() {
        return "failed to generate class: \n" +
                "    Element that caused class generation: " + getDetailedTargetName() + "\n" +
                "    Error message: " + message + "\n" +
                "    (this error is probably caused by a bug in Lighter, not your code)";
    }

    @Override
    public Element getTarget() {
        return target;
    }

    @Override
    public String getDetail() {
        return message;
    }
}
