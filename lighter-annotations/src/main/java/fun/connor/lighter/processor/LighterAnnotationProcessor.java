package fun.connor.lighter.processor;

import com.google.auto.service.AutoService;
import fun.connor.lighter.declarative.*;
import fun.connor.lighter.processor.error.AbstractCompilerError;
import fun.connor.lighter.processor.model.ValidationReport;
import fun.connor.lighter.processor.step.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.*;


@AutoService(Processor.class)
public class LighterAnnotationProcessor extends AbstractProcessor {


    private List<CompilerStep> steps;

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);

        steps = new ArrayList<>();

        steps.add(new ValidationStep(env));
        steps.add(new BuildModelStep(env));
        steps.add(new CollectRequestGuardProducersStep(env));
        steps.add(new ValidateModelStep(env));
        steps.add(new CollectDependenciesStep(env));
        steps.add(new CodeGenerationStep(env));
        steps.add(new ReverseInjectorCodeGenerationStep(env));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, Object> stepEnv = new HashMap<>();

        stepEnv.put("annotations", annotations);

        for (CompilerStep step : steps) {
            step.validateEnv(stepEnv);
            StepResult result = step.process(roundEnv);

            if (result.getErrors().isPresent()) {
                ValidationReport report = result.getErrors().get();
                if (report.containsErrors()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, report.toString());
                    return true;
                }
            }
//            if (!result.getErrors().isEmpty()) {
//                for (AbstractCompilerError error : result.getErrors()) {
//                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error.toString());
//                }
//                return true;
//            }

            if (result.hasResult()) {
                stepEnv.put(result.getResultName(), result.getResult());
            }
        }


        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Body.class.getCanonicalName());
        annotations.add(Delete.class.getCanonicalName());
        annotations.add(Get.class.getCanonicalName());
        annotations.add(Post.class.getCanonicalName());
        annotations.add(Put.class.getCanonicalName());
        annotations.add(QueryParams.class.getCanonicalName());
        annotations.add(ResourceController.class.getCanonicalName());
        annotations.add(ProducesRequestGuard.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
