package fun.connor.lighter.processor.error;

import javax.lang.model.element.Element;

public class PreformattedError extends AbstractCompilerError {

    private final String message;

    public PreformattedError(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public Element getTarget() {
        return null;
    }

    @Override
    public String getDetail() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PreformattedError) {
            return message.equals(((PreformattedError) o).message);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}
