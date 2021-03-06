package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.generator.*;
import fun.connor.lighter.compiler.model.Controller;
import fun.connor.lighter.compiler.model.Endpoint;
import fun.connor.lighter.compiler.model.Model;
import fun.connor.lighter.compiler.model.RequestGuards;
import fun.connor.lighter.compiler.validation.ValidationError;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.compiler.validation.cause.ErrorCause;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates the Java source code from the application model. This step must run
 * after the {@link ValidateModelStep} as it assumes the invariants check by it are
 * always held. This step is has side effects as it creates new source files.
 */
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
        if (model.getControllers().isEmpty()) {
            return new StepResult(); //TODO: better way to handle no-op cases
        }
        ValidationReport.Builder validationReportBuilder = ValidationReport.builder();

        List<GeneratedEndpoint> endpointHandlers = new ArrayList<>();
        for (Controller c : model.getControllers()) {
            ControllerDataContainerGenerator containerGenerator =
                    new ControllerDataContainerGenerator(env.getFiler(), c);

            try {
                containerGenerator.generateCodeFile();
            } catch (IOException e) {
                validationReportBuilder.addError(new ValidationError(e.getMessage(), ErrorCause.CODE_GENERATION_ERROR));
            }

            endpointHandlers.addAll(generateEndpoints(c, validationReportBuilder));
        }


        //TODO: this really should be written to a config file that can be picked up at runtime
        //TODO: that would allow multiple route configurations in the same JAR. This could be useful in
        //TODO: cases where different controllers are in different jars that get packaged together.
        //TODO: For now, the route generator writes to a hard-coded location. This will change post-MVP.
        RouteConfigurationGenerator routeGenerator = new RouteConfigurationGenerator(env.getFiler(), endpointHandlers);
        try {
            routeGenerator.generateCodeFile();
        } catch (IOException e) {
            validationReportBuilder.addError(new ValidationError(e.getMessage(), ErrorCause.CODE_GENERATION_ERROR));
        }

        return new StepResult(validationReportBuilder.build());
    }

    private List<GeneratedEndpoint> generateEndpoints(Controller c, ValidationReport.Builder reportBuilder) {
        List<GeneratedEndpoint> generatedTypes = new ArrayList<>();
        for (Endpoint e : c.getEndpoints()) {
            EndpointResolverGenerator generator =
                    new EndpointResolverGenerator(c, e, requestGuards, types, env.getFiler());

            try {
                GeneratedType type = generator.generateCodeFile();
                generatedTypes.add(new GeneratedEndpoint(e, type));
            } catch (IOException exception) {
                reportBuilder.addError(new ValidationError(exception.getMessage(), ErrorCause.CODE_GENERATION_ERROR));
            }
        }
        return generatedTypes;
    }
}
