package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.error.AbstractCompilerError;
import fun.connor.lighter.compiler.error.CodeGenerationError;
import fun.connor.lighter.compiler.generator.*;
import fun.connor.lighter.compiler.model.Controller;
import fun.connor.lighter.compiler.model.Endpoint;
import fun.connor.lighter.compiler.model.Model;
import fun.connor.lighter.compiler.model.RequestGuards;
import fun.connor.lighter.compiler.validation.ValidationError;
import fun.connor.lighter.compiler.validation.ValidationReport;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeGenerationStep extends CompilerStep {

    private Model model;
    private RequestGuards requestGuards;

    public CodeGenerationStep(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Set<EnvironmentRequirement> getRequiredEnv() {
        EnvironmentRequirement<Model> modelReq =
                new EnvironmentRequirement<>("model", Model.class, this::setModel);

        EnvironmentRequirement<RequestGuards> requestGuardsReq =
                new EnvironmentRequirement<>("requestGuards", RequestGuards.class, this::setRequestGuards);

        Set<EnvironmentRequirement> requirements = new HashSet<>();
        requirements.add(modelReq);
        requirements.add(requestGuardsReq);
        return requirements;
    }

    private void setModel(Model m) {
        this.model = m;
    }

    private void setRequestGuards(RequestGuards requestGuards) {
        this.requestGuards = requestGuards;
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

        //TODO: stopgap while refactoring all error reporting
        ValidationReport.Builder reportBuilder = ValidationReport.builder();

        for (AbstractCompilerError error : errors) {
            reportBuilder.addError(new ValidationError(error.toString()));
        }

        return new StepResult(reportBuilder.build());
    }

    private List<GeneratedEndpoint> generateEndpoints(Controller c, Set<AbstractCompilerError> errors) {
        List<GeneratedEndpoint> generatedTypes = new ArrayList<>();
        for (Endpoint e : c.getEndpoints()) {
            EndpointResolverGenerator generator =
                    new EndpointResolverGenerator(c, e, requestGuards, types, env.getFiler());

            try {
                GeneratedType type = generator.generateCodeFile();
                generatedTypes.add(new GeneratedEndpoint(e, type));
            } catch (IOException e1) {
                errors.add(new CodeGenerationError(e.getMethodElement(), e1.getMessage()));
            }
        }
        return generatedTypes;
    }
}
