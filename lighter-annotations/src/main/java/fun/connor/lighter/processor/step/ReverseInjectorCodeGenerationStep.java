package fun.connor.lighter.processor.step;

import fun.connor.lighter.processor.LighterTypes;
import fun.connor.lighter.processor.generator.injection.ReverseInjectorGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ReverseInjectorCodeGenerationStep extends CompilerStep {

    private Set<DeclaredType> dependencies;
    private LighterTypes types;

    public ReverseInjectorCodeGenerationStep(ProcessingEnvironment env) {
        super(env);
        this.types = new LighterTypes(env.getTypeUtils(), env.getElementUtils());
    }

    @Override @SuppressWarnings("unchecked")
    public Set<EnvironmentRequirement> getRequiredEnv() {
        EnvironmentRequirement<Set> depsReq =
                new EnvironmentRequirement<>("dependencies", Set.class, this::setDependencies);

        Set<EnvironmentRequirement> requirements = new HashSet<>();
        requirements.add(depsReq);
        return requirements;
    }

    private void setDependencies(Set<DeclaredType> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {
        if (!dependencies.isEmpty()) {
            ReverseInjectorGenerator generator = new ReverseInjectorGenerator(dependencies, types, env.getFiler());
            try {
                generator.generateCodeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new StepResult(new HashSet<>());
    }
}
