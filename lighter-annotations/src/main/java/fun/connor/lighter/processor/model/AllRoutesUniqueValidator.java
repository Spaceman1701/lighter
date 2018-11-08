package fun.connor.lighter.processor.model;

import com.google.common.collect.Lists;
import fun.connor.lighter.processor.Combinations;

import java.util.List;

public class AllRoutesUniqueValidator implements Validatable {

    private List<Route> routes;

    public AllRoutesUniqueValidator(List<Route> routes) {
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
