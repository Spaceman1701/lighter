package fun.connor.lighter.processor.step;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class CompilerStep {

    protected ProcessingEnvironment env;

    protected static class EnvironmentRequirement<T> {
        final String name;
        final Class<T> type;
        final Consumer<T> handler;

        EnvironmentRequirement(String name, Class<T> type, Consumer<T> handler) {
            this.name = name;
            this.type = type;
            this.handler = handler;
        }
    }

    protected CompilerStep(ProcessingEnvironment env) {
        this.env = env;
    }

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

    public abstract Set<EnvironmentRequirement> getRequiredEnv();

    public abstract StepResult process(RoundEnvironment roundEnv);
}
