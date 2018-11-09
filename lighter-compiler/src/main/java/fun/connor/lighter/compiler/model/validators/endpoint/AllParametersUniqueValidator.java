package fun.connor.lighter.compiler.model.validators.endpoint;

import fun.connor.lighter.compiler.model.QueryParams;
import fun.connor.lighter.compiler.model.Validatable;
import fun.connor.lighter.compiler.model.ValidationError;
import fun.connor.lighter.compiler.model.ValidationReport;

import java.util.*;

public class AllParametersUniqueValidator implements Validatable {


    private final Map<String, String> pathParams;
    private final Map<String, String> queryParams;

    public AllParametersUniqueValidator(Map<String, String> pathParams, QueryParams queryParams) {
        this.pathParams = pathParams;
        if (queryParams != null) {
            this.queryParams = queryParams.getNameMappings();
        } else {
            this.queryParams = new HashMap<>();
        }
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        List<String> params = new ArrayList<>(pathParams.values());
        params.addAll(queryParams.values());

        Set<String> uniqueSet = new HashSet<>();

        for (String s : params) {
            if (!uniqueSet.add(s)) {
                reportBuilder.addError(new ValidationError(duplicateParamsMessage(s)));
            }
        }
    }

    private String duplicateParamsMessage(String parameter) {
        return "the route has parameters that map to the same method argument. Parameter name: " + parameter;
    }
}
