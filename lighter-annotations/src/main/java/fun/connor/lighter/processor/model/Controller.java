package fun.connor.lighter.processor.model;

import jdk.internal.dynalink.support.TypeUtilities;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.List;

public class Controller {

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
}
