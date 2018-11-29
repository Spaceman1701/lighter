package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.model.Controller;
import fun.connor.lighter.compiler.model.Model;
import fun.connor.lighter.compiler.model.RequestGuardFactory;
import fun.connor.lighter.compiler.model.RequestGuards;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.DeclaredType;
import java.util.HashSet;
import java.util.Set;

/**
 * Collects all the dependencies of application classes that will have to be constructed by Lighter.
 */
public class CollectDependenciesStep extends CompilerStep {

    private Model model;
    private RequestGuards requestGuards;

    public CollectDependenciesStep(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Set<EnvironmentRequirement> getRequiredEnv() {
        //TODO: fix duplicate code
        EnvironmentRequirement<Model> modelReq =
                new EnvironmentRequirement<>("model", Model.class, this::setModel);
        EnvironmentRequirement<RequestGuards> requestGuardsReq =
                new EnvironmentRequirement<>("requestGuards", RequestGuards.class, this::setRequestGuards);

        Set<EnvironmentRequirement> requirements = new HashSet<>();
        requirements.add(modelReq);
        requirements.add(requestGuardsReq);
        return requirements;
    }

    private void setModel(Model model) {
        this.model = model;
    }

    private void setRequestGuards(RequestGuards requestGuards) {
        this.requestGuards = requestGuards;
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {
        Set<DeclaredType> dependencies = new HashSet<>(); //TODO: ensure object equality is sufficient here

        for (Controller c : model.getControllers()) {
            dependencies.add((DeclaredType) env.getTypeUtils().erasure(c.getElement().asType()));
        }

        for (RequestGuardFactory guardFactory : requestGuards.getAll()) {
            dependencies.add(guardFactory.getType());
        }

        return new StepResult("dependencies", dependencies);
    }
}
