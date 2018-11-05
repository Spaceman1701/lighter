package fun.connor.lighter.processor.step;

import fun.connor.lighter.declarative.ProducesRequestGuard;
import fun.connor.lighter.processor.MoreTypes;
import fun.connor.lighter.processor.model.RequestGuardFactory;
import fun.connor.lighter.processor.model.RequestGuards;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.util.*;

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
        Map<DeclaredType, RequestGuardFactory> guards = new HashMap<>();
        for (Element e : requestGuards) {
            DeclaredType produces = getProducesType(e);
            RequestGuardFactory requestGuardFactory = new RequestGuardFactory((DeclaredType) e.asType(), produces);
            guards.put(produces, requestGuardFactory);
        }
        return new StepResult(new HashSet<>(), "requestGuards" , new RequestGuards(guards));
    }

    private DeclaredType getProducesType(Element e) {
        for (AnnotationMirror mirror : e.getAnnotationMirrors()) {
            if (MoreTypes.isTypeMirrorOfClass(mirror.getAnnotationType(), ProducesRequestGuard.class)) {
                return getAnnotationValue(mirror).orElseThrow(() -> new IllegalArgumentException("uhg"));
            }
        }
        return null;
    }

    private Optional<DeclaredType> getAnnotationValue(AnnotationMirror annotation) {
        return annotation.getElementValues().entrySet().stream()
                .filter(ex -> ex.getKey().getSimpleName().toString().equals("value"))
                .map(Map.Entry::getValue)
                .map(av -> (DeclaredType)av.getValue())
                .findAny();
    }
}
