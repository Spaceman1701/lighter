package fun.connor.lighter.processor.model.validators;


import fun.connor.lighter.processor.model.QueryParams;
import fun.connor.lighter.processor.model.Route;
import fun.connor.lighter.processor.model.endpoint.MethodParameter;
import fun.connor.lighter.processor.model.validators.endpoint.AllParametersUniqueValidator;
import fun.connor.lighter.processor.model.validators.endpoint.AllParamsExistValidator;

import java.util.List;
import java.util.Map;

public final class Validators {
    private Validators() {};

    public static AllRoutesUniqueValidator allRoutesUnique(List<Route> routes) {
        return new AllRoutesUniqueValidator(routes);
    }

    public static AllEndpointsUniqueValidator allEndpointsUnique(List<fun.connor.lighter.processor.model.Endpoint> endpoints) {
        return new AllEndpointsUniqueValidator(endpoints);
    }

    public static class Endpoint {
        private Endpoint() {}

        public static AllParamsExistValidator allParamsExist(Map<String, String> params, Map<String, MethodParameter> methodParams) {
            return new AllParamsExistValidator(params, methodParams);
        }

        public static AllParametersUniqueValidator allParametersUnique(Map<String, String> pathParams, QueryParams queryParams) {
            return new AllParametersUniqueValidator(pathParams, queryParams);
        }
    }
}
