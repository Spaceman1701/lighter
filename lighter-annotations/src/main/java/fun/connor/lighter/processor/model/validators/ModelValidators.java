package fun.connor.lighter.processor.model.validators;


import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.Route;

import java.util.List;

public final class ModelValidators {
    private ModelValidators() {};

    public static AllRoutesUniqueValidator allRoutesUnique(List<Route> routes) {
        return new AllRoutesUniqueValidator(routes);
    }

    public static AllEndpointsUniqueValidator allEndpointsUnique(List<Endpoint> endpoints) {
        return new AllEndpointsUniqueValidator(endpoints);
    }
}
