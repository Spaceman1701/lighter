package fun.connor.lighter.compiler.model.validators;

import fun.connor.lighter.compiler.Combinations;
import fun.connor.lighter.compiler.model.Endpoint;
import fun.connor.lighter.compiler.model.Validatable;
import fun.connor.lighter.compiler.model.ValidationError;
import fun.connor.lighter.compiler.model.ValidationReport;

import java.util.List;

public class AllEndpointsUniqueValidator implements Validatable {

    private List<Endpoint> endpoints;

    public AllEndpointsUniqueValidator(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        Combinations.CombinationsOf(endpoints).forEach(p -> {
            Endpoint a = p.first;
            Endpoint b = p.second;
            if (!a.equals(b) && a.isSameEndpoint(b)) {
                reportBuilder.addError(new ValidationError(makeDuplicateRoutesMessage(a, b)));
            }
        });
    }

    private String makeDuplicateRoutesMessage(Endpoint a, Endpoint b) {
        return "There indistinguishable Endpoints. At least one must be changed: \n" +
                "  First: " + a.getHttpMethod() + " " + a.pathTemplate() + " found at: \n" +
                "    " + a.getFullName() + "\n" +
                "  Second: " + b.getHttpMethod() + " " + b.pathTemplate() + " found at: \n" +
                "    " + b.getFullName();
    }
}