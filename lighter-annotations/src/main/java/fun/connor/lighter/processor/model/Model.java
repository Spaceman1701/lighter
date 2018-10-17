package fun.connor.lighter.processor.model;

import java.util.List;

public class Model {
    private List<Controller> controllers;

    public Model(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public List<Controller> getControllers() {
        return controllers;
    }
}
