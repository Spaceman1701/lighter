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
    }

    private Route(List<RoutePart> parts) {
        this.templateStr = "";
        this.params = new HashMap<>();
        this.parts = parts;
    }


    public Route append(Route other) {
        List<RoutePart> newParts = new ArrayList<>(this.parts);
        newParts.addAll(other.parts);
        return new Route(newParts);
    }

    /**
     * Check if this route captures all cases of another
     * route at an equal level of specificity
     *
     * @param other the other route
     * @return true iff no precedence can be resolved between the two routes
     */
    public boolean captures(Route other) {
        return false;
    }

    public String getTemplateStr() {
        return templateStr;
    }
}
