package fun.connor.lighter.processor.model;

public class ParameterParser {

    private final String exposedName;
    private final String nameOnMethod;

    public ParameterParser(String param) {
        String[] parts = param.split(":");
        if (parts.length == 1) {
            exposedName = parts[0];
            nameOnMethod = parts[0];
        } else {
            exposedName = parts[0];
            nameOnMethod = parts[1];
        }
    }

    public String getExposedName() {
        return exposedName;
    }

    public String getNameOnMethod() {
        return nameOnMethod;
    }
}
