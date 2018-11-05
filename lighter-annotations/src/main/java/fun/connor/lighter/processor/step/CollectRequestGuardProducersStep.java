package fun.connor.lighter.processor.step;

import fun.connor.lighter.declarative.ProducesRequestGuard;
import fun.connor.lighter.handler.RequestGuard;
import fun.connor.lighter.processor.model.RequestGuardFactory;
import fun.connor.lighter.processor.model.RequestGuards;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollectRequestGuardProducersStep extends CompilerStep {

    public CollectRequestGuardProducersStep(ProcessingEnvironment env) {
        super(env);
    }

    @Override
    public Set<EnvironmentRequirement> getRequiredEnv() {
        return new HashSet<>();
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {
        Set<? extends Element> requestGuards = roundEnv.getElementsAnnotatedWith(ProducesRequestGuard.class);
        Map<Class, RequestGuardFactory> guards = new HashMap<>();
        for (Element e : requestGuards) {
            ProducesRequestGuard annotation = e.getAnnotation(ProducesRequestGuard.class);
            Class<? extends RequestGuard> produces = annotation.value();
            RequestGuardFactory requestGuardFactory = new RequestGuardFactory((DeclaredType) e.asType(), produces);
            guards.put(produces, requestGuardFactory);
        }
        return new StepResult(new HashSet<>(), "requestGuards" , new RequestGuards(guards));
    }
}
