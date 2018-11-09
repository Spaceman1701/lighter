package fun.connor.lighter.compiler.generator;

import java.util.Objects;

public final class Utils {

    private Utils() {}

    public static String decapitalize(String s) {
        if (Objects.isNull(s) || s.isEmpty()) {
            return s;
        }

        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static String capitalize(String s) {
        if (Objects.isNull(s) || s.isEmpty()) {
            return s;
        }

        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
