package fun.connor.lighter.processor.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
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
}
