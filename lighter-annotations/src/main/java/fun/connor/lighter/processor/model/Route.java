package fun.connor.lighter.processor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {

    private Map<String, String> params;
    private List<RoutePart> parts;
    private String templateStr;


    public Route(String templateStr) {
        this.templateStr = templateStr;
        this.params = new HashMap<>();
        this.parts = new ArrayList<>();

        parsePartsFromTemplate(templateStr);
        parseFromRouteParts(parts);

        System.out.println("built route: " + templateStr);
    }

    private Route(List<RoutePart> parts) {
        this.templateStr = "";
        this.params = new HashMap<>();
        this.parts = parts;

        parseFromRouteParts(parts);

        System.out.println("built route (from parts): " + templateStr);
    }

    private void parsePartsFromTemplate(String templateStr) {
        String[] splitTemplate = templateStr.split("/");
        for (String s : splitTemplate) {
            if (!s.isEmpty()) {
                if (isParameter(s)) {
                    String strippedString = s.replace("{", "").replace("}", "");
                    parts.add(new RoutePart(strippedString, RoutePart.Kind.PARAMETER));
                } else if (isWildcard(s)) {
                    parts.add(new RoutePart(s, RoutePart.Kind.WILDCARD));
                } else {
                    parts.add(new RoutePart(s, RoutePart.Kind.NORMAL));
                }
            }
        }
    }

    private boolean isWildcard(String templatePart) {
        return templatePart.equals("*") || templatePart.equals("**");
    }

    private boolean isParameter(String templatePart) {
        return templatePart.startsWith("{") && templatePart.endsWith("}");
    }

    private void parseFromRouteParts(List<RoutePart> parts) {
        StringBuilder templateBuilder = new StringBuilder();

        for (RoutePart p : parts) {
            templateBuilder.append(p.getString());
            templateBuilder.append("/");
            if (p.getKind() == RoutePart.Kind.PARAMETER) {
                ParameterParser parser = new ParameterParser(p.getString());
                params.put(parser.getExposedName(), parser.getNameOnMethod());
            }
        }

        templateStr = templateBuilder.toString();
    }

    public Route append(Route other) {
        List<RoutePart> newParts = new ArrayList<>(this.parts);
        newParts.addAll(other.parts);
        return new Route(newParts);
    }

    /**
     * Check if this route covers ALL cases of another
     * route.
     */
    public boolean captures(Route other) {
        return false;
    }

    /**
     * Route specificity determines the match order for overlapping routes
     *
     * /foo/bar/123 - specificity = 3
     * /foo/* /123 - specificity = 2
     * /foo/* -specificity = 1
     * @return the route specificity
     */
    public int getSpecificity() {
        int specificity = 0;
        for (RoutePart p : parts) {
            if (p.getKind() == RoutePart.Kind.NORMAL) {
                specificity++;
            }
        }
        return specificity;
    }

    public String getTemplateStr() {
        return templateStr;
    }

    public Map<String, String> getParams() {
        return params;
    }


    /**
     * Two routes are equal iff they identify the same resources
     * at the same specificity
     * @param o the other object
     * @return true if the routes are equal
     */
    public boolean identityEquals(Object o) {
        if (o instanceof Route) {
            Route other = (Route) o;
            return captures(other) && getSpecificity() == other.getSpecificity();
        }
        return false;
    }
}
