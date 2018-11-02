package fun.connor.lighter.processor.step;

import fun.connor.lighter.processor.error.AbstractCompilerError;
import fun.connor.lighter.processor.error.CodeGenerationError;
import fun.connor.lighter.processor.generator.*;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Endpoint;
import fun.connor.lighter.processor.model.Model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeGenerationStep extends CompilerStep {

    private Model model;

    public CodeGenerationStep(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Set<EnvironmentRequirement> getRequiredEnv() {
        EnvironmentRequirement<Model> req =
                new EnvironmentRequirement<>("model", Model.class, this::setModel);
        Set<EnvironmentRequirement> requirements = new HashSet<>();
        requirements.add(req);
        return requirements;
    }

    private void setModel(Model m) {
        this.model = m;
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {
        Set<AbstractCompilerError> errors = new HashSet<>();

        List<GeneratedEndpoint> endpointHandlers = new ArrayList<>();
        for (Controller c : model.getControllers()) {
            ControllerDataContainerGenerator containerGenerator =
                    new ControllerDataContainerGenerator(env.getFiler(), c);

            try {
                containerGenerator.generateCodeFile();
            } catch (IOException e) {
                errors.add(new CodeGenerationError(c.getElement(), e.getMessage()));
            }

            endpointHandlers.addAll(generateEndpoints(c, errors));
        }


        //TODO: this really should be written to a config file that can be picked up at runtime
        //TODO: that would allow multiple route configurations in the same JAR. This could be useful in
        //TODO: cases where different controllers are in different jars that get packaged together.
        //TODO: For now, the route generator writes to a hard-coded location. This will change post-MVP.
        RouteConfigurationGenerator routeGenerator = new RouteConfigurationGenerator(env.getFiler(), endpointHandlers);
        try {
            routeGenerator.generateCodeFile();
        } catch (IOException e) {
            //TODO: error handling
        }

        return new StepResult(errors);
    }

    private List<GeneratedEndpoint> generateEndpoints(Controller c, Set<AbstractCompilerError> errors) {
        List<GeneratedEndpoint> generatedTypes = new ArrayList<>();
        for (Endpoint e : c.getEndpoints()) {
//            EndpointRequestResolverGenerator generator =
//                    new EndpointRequestResolverGenerator(c, e, typeUtils, env.getFiler());
            EndpointResolverGenerator generator2 =
                    new EndpointResolverGenerator(c, e, typeUtils, env.getFiler());

            try {
//                GeneratedType type = generator.generateCodeFile();
                GeneratedType type = generator2.generateCodeFile();
                generatedTypes.add(new GeneratedEndpoint(e, type));
            } catch (IOException e1) {
                errors.add(new CodeGenerationError(e.getMethodElement(), e1.getMessage()));
            }
        }
        return generatedTypes;
    }
}
