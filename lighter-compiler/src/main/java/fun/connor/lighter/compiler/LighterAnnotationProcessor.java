package fun.connor.lighter.compiler;

import com.google.auto.service.AutoService;
import fun.connor.lighter.compiler.step.*;
import fun.connor.lighter.compiler.validation.ReportPrinter;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.declarative.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.*;

/**
 * The Lighter annotation processor. All Lighter annotations are processed
 * by this processor. Individual operations are delegated to {@link CompilerStep}
 * objects. Each CompilerStep object is essentially a single-purpose annotation
 * processor.
 *
 * The advantage of this implementation is that new steps can be added without adding
 * new processors.
 *
 * See {@link Processor} and {@link AbstractProcessor}
 */
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
        ReportPrinter reportPrinter = new ReportPrinter(processingEnv.getMessager());
        Map<String, Object> stepEnv = new HashMap<>();

        stepEnv.put("annotations", annotations);

        for (CompilerStep step : steps) {
            step.validateEnv(stepEnv);
            StepResult result = step.process(roundEnv);


            boolean wereErrors = result.getErrors()
                    .map(r -> handleStepErrors(reportPrinter, r))
                    .orElse(false);
            if (wereErrors) {
                return true;
            }

            if (result.hasResult()) {
                stepEnv.put(result.getResultName(), result.getResult());
            }
        }


        return false;
    }

    private boolean handleStepErrors(ReportPrinter printer, ValidationReport report) {
        if (report.containsErrors()) {
            report.print(printer);
            return true;
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
