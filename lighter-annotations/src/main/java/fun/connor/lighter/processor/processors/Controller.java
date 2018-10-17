package fun.connor.lighter.processor.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private TypeElement element;

    private List<Endpoint> endpoints;

    public Controller(TypeElement element, List<Endpoint> endpoints) {
        this.element = element;
        this.endpoints = endpoints;
    }
}
