package fun.connor.lighter.processor.model;

import com.google.common.collect.Lists;
import fun.connor.lighter.processor.Combinations;

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
            c.validate(reportBuilder);
        }

        ValidationReport.Builder uniqueValidatorReport = ValidationReport.builder(); //context-free validator
        validateControllerStubs(uniqueValidatorReport);
        reportBuilder.addChild(uniqueValidatorReport);

        ValidationReport.Builder uniqueRoutesReport = ValidationReport.builder();
        validateNoDuplicateRoutes(uniqueRoutesReport);
        reportBuilder.addChild(uniqueRoutesReport);

    }

    private void validateControllerStubs(ValidationReport.Builder report) {
        AllRoutesUniqueValidator uniqueValidator = new AllRoutesUniqueValidator(
                controllers.stream()
                        .map(Controller::getRouteFragment)
                        .collect(Collectors.toList())
        );
        uniqueValidator.validate(report);
    }

    private void validateNoDuplicateRoutes(ValidationReport.Builder report) {
        List<Endpoint> endpoints = controllers.stream()
                .map(Controller::getEndpoints)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Combinations.CombinationsOf(endpoints).forEach(p -> {
                    Endpoint a = p.first;
                    Endpoint b = p.second;
                    if (!a.equals(b) && a.isSameEndpoint(b)) {
                        report.addError(new ValidationError(makeDuplicateRoutesMessage(a, b)));
                    }
                });
    }

    private String makeDuplicateRoutesMessage(Endpoint a, Endpoint b) {
        return "There indistinguishable Endpoints. At least one must be changed: \n" +
                "  First: " + a.getHttpMethod() + " " + a.pathTemplate() + " found at: \n" +
                "    " + a.getMethodName() + "\n" +
                "  Second: " + b.getHttpMethod() + " " + b.pathTemplate() + " found at: \n" +
                "    " + b.getMethodName();
    }

    private String makeControllerContext(Controller c) {
        return "At " + c.getSimpleName();
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
