package fun.connor.lighter.processor.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Route {

    private Map<String, String> params;
    private List<RoutePart> parts;
    private String templateStr;


    public Route(String templateStr) {

    }

    private Route(List<RoutePart> parts) {
        //reconstruct template string
    }


    public Route append(Route other) {
        List<RoutePart> newParts = new ArrayList<>(this.parts);
        newParts.addAll(other.parts);
        return new Route(newParts);
    }
}
