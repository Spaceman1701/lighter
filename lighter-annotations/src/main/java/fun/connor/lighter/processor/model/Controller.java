package fun.connor.lighter.processor.model;

import javax.lang.model.element.TypeElement;
import java.util.List;

public class Controller implements Validatable {

    private TypeElement element;
    private Route routeFragment;
    private List<Endpoint> endpoints;

    public Controller(TypeElement element, List<Endpoint> endpoints, Route routeFragment) {
        this.element = element;
        this.routeFragment = routeFragment;
        this.endpoints = endpoints;
    }

    public Route getRouteFragment() {
        return routeFragment;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public String getFullName() {
        return element.getQualifiedName().toString();
    }

    public String getContainingName() {
        String fullName = getFullName();
        int finalDotIndex = fullName.lastIndexOf('.');
        return fullName.substring(0, finalDotIndex);
    }

    public String getSimpleName() {
        return element.getSimpleName().toString();
    }

    public TypeElement getElement() {
        return element;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Controller)) {
            return false;
        }
        return element.equals(((Controller) other).element);
    }

    @Override
    public void validate(ValidationReport.Builder reportBuilder) {
        for (Endpoint e : endpoints) {
            ValidationReport.Builder endpointReport = ValidationReport.builder("at method " + e.getMethodName());
            e.validate(endpointReport);
            reportBuilder.addChild(endpointReport);
        }
    }
}
