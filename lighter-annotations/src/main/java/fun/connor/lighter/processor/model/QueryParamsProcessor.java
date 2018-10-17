package fun.connor.lighter.processor.model;

import java.util.HashMap;
import java.util.Map;

public class QueryParamsProcessor {

    private Map<String, String> nameMappings;

    public QueryParamsProcessor(String[] params) {
        nameMappings = new HashMap<>();
        parseParams(params);
    }

    private void parseParams(String[] params) {
        for (String s : params) {
            String fromName;
            String toName;
            if (s.contains(":")) {
                String[] parts = s.split(":");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("query string had illegal mapping: " + s);
                }
                fromName = parts[0];
                toName = parts[1];
            } else {
                fromName = s;
                toName = s;
            }
            nameMappings.put(fromName, toName);
        }
    }

    public Map<String, String> getNameMappings() {
        return nameMappings;
    }
}
