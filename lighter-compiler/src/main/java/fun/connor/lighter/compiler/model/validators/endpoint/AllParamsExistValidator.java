package fun.connor.lighter.compiler.model.validators.endpoint;

import fun.connor.lighter.compiler.model.Validatable;
import fun.connor.lighter.compiler.model.ValidationError;
import fun.connor.lighter.compiler.model.ValidationReport;
import fun.connor.lighter.compiler.model.endpoint.MethodParameter;

import java.util.Map;

public class AllParamsExistValidator implements Validatable {

    private Map<String, String> params;
    private Map<String, MethodParameter> methodParams;

    public AllParamsExistValidator
            (Map<String, String> params, Map<String, MethodParameter> methodParams) {
        this.params = params;
        this.methodParams = methodParams;
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        params.keySet().forEach(p -> {
            String nameOnMethod = params.get(p);
            if (!methodParams.containsKey(nameOnMethod)) {
                reportBuilder.addError(new ValidationError(missingKeyMessage(p)));
            }
        });
    }

    private String missingKeyMessage(String param) {
        return "parameter \'" + param + "\' is missing it's mapped method parameter, \'" + params.get(param) + "\'";
    }
}