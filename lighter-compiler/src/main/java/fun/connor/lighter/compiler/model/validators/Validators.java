package fun.connor.lighter.compiler.model.validators;


import fun.connor.lighter.compiler.model.QueryParams;
import fun.connor.lighter.compiler.model.Route;
import fun.connor.lighter.compiler.model.MethodParameter;
import fun.connor.lighter.compiler.model.validators.endpoint.AllParametersUniqueValidator;
import fun.connor.lighter.compiler.model.validators.endpoint.AllParamsExistValidator;

import java.util.List;
import java.util.Map;

public final class Validators {
    private Validators() {};

    public static AllRoutesUniqueValidator allRoutesUnique(List<Route> routes) {
        return new AllRoutesUniqueValidator(routes);
    }

    public static AllEndpointsUniqueValidator allEndpointsUnique(List<fun.connor.lighter.compiler.model.Endpoint> endpoints) {
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
