package fun.connor.lighter.processor.model;

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

        AllRoutesUniqueValidator uniqueValidator = new AllRoutesUniqueValidator(
                controllers.stream()
                        .map(Controller::getRouteFragment)
                        .collect(Collectors.toList())
            );

        ValidationReport.Builder uniqueValidatorReport = ValidationReport.builder(); //context-free validator
        uniqueValidator.validate(uniqueValidatorReport);
        reportBuilder.addChild(uniqueValidatorReport);
    }

    private String makeControllerContext(Controller c) {
        return "At " + c.getSimpleName();
    }
}
