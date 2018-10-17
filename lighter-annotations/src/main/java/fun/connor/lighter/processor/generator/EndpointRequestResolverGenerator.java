package fun.connor.lighter.processor.generator;

import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;

import javax.annotation.processing.Filer;
import java.io.IOException;

public class EndpointRequestResolverGenerator extends AbstractGenerator {


    private Controller controller;
    private Endpoint endpoint;

    public EndpointRequestResolverGenerator(Controller controller, Endpoint endpoint, Filer filer) {
        super(filer);
        this.controller = controller;
        this.endpoint = endpoint;
    }


    @Override
    public void generateCodeFile() throws IOException {

    }


}
