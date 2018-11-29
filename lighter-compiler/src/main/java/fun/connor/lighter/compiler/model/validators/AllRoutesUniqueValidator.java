package fun.connor.lighter.compiler.model.validators;

import fun.connor.lighter.compiler.Combinations;
import fun.connor.lighter.compiler.model.Route;
import fun.connor.lighter.compiler.validation.Validatable;
import fun.connor.lighter.compiler.validation.ValidationError;
import fun.connor.lighter.compiler.validation.ValidationReport;

import java.util.List;

/**
 * External validator that validates that all routes in a given list are differentiable.
 */
public class AllRoutesUniqueValidator implements Validatable {

    private List<Route> routes;

    AllRoutesUniqueValidator(List<Route> routes) {
        this.routes = routes;
    }


    @Override
    public void validate(ValidationReport.Builder report) {
        Combinations.CombinationsOf(routes).forEach(l ->
                {
                    Route a = l.first;
                    Route b = l.second;
                    if (!a.equals(b) && a.captures(b)) {
                        report.addError(new ValidationError(makeErrorMessage(a, b)));
                    }
                });

    }

    private String makeErrorMessage(Route a, Route b) {
        return "Found duplicate routes: \'" + a.getTemplateStr() + "\' " +
                "is the same as \'" + b.getTemplateStr() + "\'";
    }
}
