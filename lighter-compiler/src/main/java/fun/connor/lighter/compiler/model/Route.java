package fun.connor.lighter.compiler.model;

import com.google.common.collect.Streams;
import fun.connor.lighter.compiler.utils.Pair;

import java.util.*;

/**
 * Represents a route constructed from a template String. Endpoints and Controllers both have Routes.
 */
public class Route {

    private Map<String, String> params;
    private List<RoutePart> parts;
    private String templateStr;


    public Route(String templateStr) {
        this.templateStr = templateStr;
        this.params = new HashMap<>();
        this.parts = new ArrayList<>();

        parsePartsFromTemplate(templateStr);
        parseFromRouteParts(parts, hasTrailingSlash());
    }

    private Route(List<RoutePart> parts, boolean trailingSlash) {
        this.templateStr = "";
        this.params = new HashMap<>();
        this.parts = parts;

        parseFromRouteParts(parts, trailingSlash);
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
                } else if (isNormal(s)) {
                    parts.add(new RoutePart(s, RoutePart.Kind.NORMAL));
                } else {
                    throw new IllegalArgumentException("unparseable template part: " + templateStr);
                }
            }
        }
    }

    private boolean isWildcard(String templatePart) {
        return templatePart.equals("*");
    }

    private boolean isParameter(String templatePart) {
        return templatePart.startsWith("{") && templatePart.endsWith("}");
    }

    private boolean isNormal(String templatePart) {
        return !(templatePart.contains("*")
                || templatePart.contains(":")
                || templatePart.contains("/"));
    }

    private void parseFromRouteParts(List<RoutePart> parts, boolean trailingSlash) {
        StringBuilder templateBuilder = new StringBuilder();

        for (RoutePart p : parts) {
            if (p.getKind() == RoutePart.Kind.PARAMETER) {
                ParameterParser parser = new ParameterParser(p.getString());
                if (params.containsKey(parser.getExposedName())) {
                    throw new IllegalArgumentException("duplicate route parameter name");
                }
                params.put(parser.getExposedName(), parser.getNameOnMethod());
                templateBuilder.append("{").append(p.getString()).append("}");
            } else {
                templateBuilder.append(p.getString());
            }
            templateBuilder.append("/");

        }
        templateStr = templateBuilder.toString();
        if (!trailingSlash && !templateStr.isEmpty()) {
            templateStr = templateStr.substring(0, templateStr.length() - 1);
        }
    }

    public Route append(Route other) {
        List<RoutePart> newParts = new ArrayList<>(this.parts);
        newParts.addAll(other.parts);
        return new Route(newParts, other.hasTrailingSlash());
    }

    public String getTemplateWithSimpleNames() {
        String simpleTemplate = templateStr;
        for (RoutePart part : parts) {
            if (part.getKind() == RoutePart.Kind.PARAMETER) {
                ParameterParser parser = new ParameterParser(part.getString());
                String simpleParamName = "{" + parser.getExposedName() + "}";
                String fullParamName = "{" + part.getString() + "}";
                simpleTemplate = simpleTemplate.replace(fullParamName, simpleParamName);
            }
        }
        return simpleTemplate;
    }

    /**
     * Check if this route covers ALL cases of another
     * route.
     *
     * @param other the endpoint to compare
     * @return true iff these endpoints are semantically indistinguishable
     */
    public boolean captures(Route other) {
        Objects.requireNonNull(other);
        if (other.getSpecificity() != this.getSpecificity()) {
            return false; //routes with different specificity can never fully capture
        }

        boolean routesEqual = Streams.zip(parts.stream(), other.parts.stream(), Pair::new)
                .map((p) -> {
                    RoutePart a = p.getFirst();
                    RoutePart b = p.getSecond();
                    if (a.getKind() == b.getKind()) {
                        if (a.getKind() != RoutePart.Kind.PARAMETER) {
                            return a.getString().equals(b.getString());
                        } else {
                            return true;
                        }
                    }
                    return false;
                })
                .reduce(Boolean::logicalAnd).orElse(false);


        return routesEqual;
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


    public boolean hasTrailingSlash() {
        if (templateStr.isEmpty()) {
            return false;
        }
        return templateStr.charAt(templateStr.length() - 1) == '/';
    }

    /**
     * Check that the given template string is syntactically correct. I.e. that no
     * exceptions will be thrown if a Route is constructed from this string.
     * @param templateStr the path template string to check
     * @return just an error message if there is an error or nothing if there is not
     */
    //TODO: find a better way to do this
    public static Optional<String> checkRouteTemplateSyntax(String templateStr) {
        try {
            new Route(templateStr);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e.getMessage());
        }
    }
}
