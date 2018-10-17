package fun.connor.lighter.processor.step;

import fun.connor.lighter.processor.error.CompilerError;
import fun.connor.lighter.processor.generator.ControllerDataContainerGenerator;
import fun.connor.lighter.processor.model.Controller;
import fun.connor.lighter.processor.model.Model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.io.IOException;
import java.util.HashSet;
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
        Set<CompilerError> errors = new HashSet<>();
        for (Controller c : model.getControllers()) {
            ControllerDataContainerGenerator containerGenerator =
                    new ControllerDataContainerGenerator(env.getFiler(), c);

            try {
                containerGenerator.generateCode();
            } catch (IOException e) {
                errors.add(new CompilerError(null, e.getMessage(), null));
            }
        }

        return new StepResult(errors);
    }
}
