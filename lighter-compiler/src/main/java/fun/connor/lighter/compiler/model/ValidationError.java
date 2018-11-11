package fun.connor.lighter.compiler.model;

public class ValidationError implements ReportFormatable {
    private String message;

    public ValidationError(String message) {
        this.message = message;
    }

    @Override
    public String toStringRelative(String prefix) {
        String[] messageByLine = message.split("\n");

        StringBuilder builder = new StringBuilder();
        for (String line : messageByLine) {
            builder.append(prefix).append(line).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return message;
    }
}