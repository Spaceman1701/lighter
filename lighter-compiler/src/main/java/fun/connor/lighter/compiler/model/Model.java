package fun.connor.lighter.compiler.model;

import fun.connor.lighter.compiler.model.validators.Validators;
import fun.connor.lighter.compiler.validation.Validatable;
import fun.connor.lighter.compiler.validation.ValidationReport;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Model implements Validatable {
    private List<Controller> controllers;

    public Model(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public List<Controller> getControllers() {
        return controllers;
    }


    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        for (Controller c : controllers) {
            ValidationReport.Builder controllerReport = ValidationReport.builder(makeControllerContext(c));
            c.validate(controllerReport);
            reportBuilder.addChild(controllerReport);
        }

        validateControllerStubs(reportBuilder);
        validateNoDuplicateRoutes(reportBuilder);

    }

    private void validateControllerStubs(ValidationReport.Builder report) {
        List<Route> routeFragments = controllers.stream()
                        .map(Controller::getRouteFragment)
                        .collect(Collectors.toList());
        Validators.allRoutesUnique(routeFragments).validate(report);
    }

    private void validateNoDuplicateRoutes(ValidationReport.Builder report) {
        List<Endpoint> endpoints = controllers.stream()
                .map(Controller::getEndpoints)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Validators.allEndpointsUnique(endpoints).validate(report);
    }


    private String makeControllerContext(Controller c) {
        return "at " + c.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Model)) {
            return false;
        }

        Model that = (Model) o;
        boolean thatContainsAll = controllers.stream()
                .map((c) -> that.getControllers().contains(c))
                .reduce(Boolean::logicalAnd).orElse(true);
        boolean thisContainsAll = that.getControllers().stream()
                .map(controllers::contains)
                .reduce(Boolean::logicalAnd).orElse(true);

        return thatContainsAll && thisContainsAll;
    }
}
