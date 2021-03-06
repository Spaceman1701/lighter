package fun.connor.lighter.compiler.model;

/**
 * Parser for path and query parameters.
 */
class ParameterParser {

    private final String exposedName;
    private final String nameOnMethod;

    ParameterParser(String param) {
        if (param.indexOf(":") == 0 || param.indexOf(":") == param.length() - 1) {
            throw new IllegalArgumentException("malformed parameter string");
        }
        String[] parts = param.split(":");
        if (parts.length == 1) {
            exposedName = parts[0];
            nameOnMethod = parts[0];
        } else if (parts.length == 2){
            exposedName = parts[0];
            nameOnMethod = parts[1];
        } else {
            throw new IllegalArgumentException("too many parts in parameter string");
        }
    }

    String getExposedName() {
        return exposedName;
    }

    String getNameOnMethod() {
        return nameOnMethod;
    }
}
