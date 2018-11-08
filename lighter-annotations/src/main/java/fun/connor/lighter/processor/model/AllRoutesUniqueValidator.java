package fun.connor.lighter.processor.model;

import com.google.common.collect.Lists;

import java.util.List;

public class AllRoutesUniqueValidator implements Validatable {

    private List<Route> routes;

    public AllRoutesUniqueValidator(List<Route> routes) {
        this.routes = routes;
    }


    @Override
    public void validate(ValidationReport.Builder report) {
        Lists.cartesianProduct(routes, routes).forEach(l ->
                {
                    Route a = l.get(0);
                    Route b = l.get(1);
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
