package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.LighterTypes;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * An individual model iteration. CompilerSteps declare dependencies on environment
 * variables (stored in a step environment map) and produce a {@link StepResult} to map
 * into the environment for future steps.
 * <br>
 * This abstract class provides enforces a template for steps to use. It provides the generic implementations
 * of environment validation for each step and declares abstract methods for step processing.
 */
public abstract class CompilerStep {

    protected ProcessingEnvironment env;
    protected LighterTypes types;

    /**
     * A description of a requirement from the environment map
     * @param <T> the type of the requirement
     */
    static class EnvironmentRequirement<T> {
        final String name;
        final Class<T> type;
        final Consumer<T> handler;

        EnvironmentRequirement(String name, Class<T> type, Consumer<T> handler) {
            this.name = name;
            this.type = type;
            this.handler = handler;
        }
    }

    /**
     * Construct this step from the processing environment
     * @param env the processing environment
     */
    CompilerStep(ProcessingEnvironment env) {
        this.env = env;
        this.types = new LighterTypes(env.getTypeUtils(), env.getElementUtils());
    }

    /**
     * Validates the provided processing environment against the {@link EnvironmentRequirement}s
     * for this class
     *
     * @param stepEnv the environment
     */
    @SuppressWarnings("unchecked") //handler.accept - this method manually does type checking
    public void validateEnv(Map<String, Object> stepEnv) {
        Set<EnvironmentRequirement> requirements = getRequiredEnv();

        for (EnvironmentRequirement requirement : requirements) {
            Object value = stepEnv.get(requirement.name);
            if (value == null) {
                throw new RuntimeException(requirement.name + " not present in environment");
            }
            if (!requirement.type.isInstance(value)) {
                throw new RuntimeException(requirement.name + " was "
                        + value.getClass().getSimpleName() + " but needed to be " + requirement.type.getSimpleName());
            }
            requirement.handler.accept(value);
        }
    }

    /**
     * Describes the required invariants about the environment for this step to run successfully
     * @return the set of requirements
     */
    public abstract Set<EnvironmentRequirement> getRequiredEnv();

    /**
     * Run the compiler step generating any validation reports our outputs from the step
     * @param roundEnv the {@link RoundEnvironment} form the processing round
     * @return a {@link StepResult} with the results from the step
     */
    public abstract StepResult process(RoundEnvironment roundEnv);
}
