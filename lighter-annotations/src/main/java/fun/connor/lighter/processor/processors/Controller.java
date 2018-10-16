package fun.connor.lighter.processor.processors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private ProcessingEnvironment env;
    private RoundEnvironment roundEnv;

    private TypeElement element;

    private List<Endpoint> endpoints;

    public Controller(ProcessingEnvironment env, RoundEnvironment roundEnv, TypeElement element) {
        this.env = env;
        this.roundEnv = roundEnv;
        this.element = element;
        endpoints = new ArrayList<>();
    }
}
