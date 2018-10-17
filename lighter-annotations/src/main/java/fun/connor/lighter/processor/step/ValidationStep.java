package fun.connor.lighter.processor.step;

import fun.connor.lighter.declarative.*;
import fun.connor.lighter.processor.error.CompilerError;
import fun.connor.lighter.processor.validators.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidationStep extends CompilerStep {

    private final AnnotationValidatorDatabase annotationValidators;

    private Set<? extends TypeElement> annotations;

    public ValidationStep(ProcessingEnvironment env) {
        super(env);
        annotationValidators = new AnnotationValidatorDatabase();
        annotationValidators.registerValidator(ResourceController.class, new ResourceControllerValidator());
        annotationValidators.registerValidator(QueryParams.class, new QueryParamsValidator(env));
        annotationValidators.registerValidator(Body.class, new BodyValidator());

        annotationValidators.registerValidator(Get.class, new EndpointAnnotationValidator(env));
        annotationValidators.registerValidator(Delete.class, new EndpointAnnotationValidator(env));
        annotationValidators.registerValidator(Post.class, new EndpointAnnotationValidator(env));
        annotationValidators.registerValidator(Put.class, new EndpointAnnotationValidator(env));
    }

    @Override @SuppressWarnings("unchecked") //TODO: resolve generic type issues
    public Set<EnvironmentRequirement> getRequiredEnv() {
        EnvironmentRequirement annotationsReq =
                new EnvironmentRequirement<>("annotations", Set.class, this::setAnnotations);
        Set<EnvironmentRequirement> requirements = new HashSet<>();
        requirements.add(annotationsReq);
        return requirements;
    }

    private void setAnnotations(Set<? extends TypeElement> annotations) {
        this.annotations = annotations;
    }

    @Override
    public StepResult process(RoundEnvironment roundEnv) {

        Map<String, Set<? extends Element>> elementsByAnnotation = new HashMap<>();

        for (TypeElement annotation : annotations) {
            String annotationName = annotation.getQualifiedName().toString();
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            elementsByAnnotation.put(annotationName, elements);
        }

        Set<CompilerError> errors  = new HashSet<>();
        for (Map.Entry<String, Set<? extends Element>> entry : elementsByAnnotation.entrySet()) {
            for (Element element : entry.getValue()) {
                try {
                    annotationValidators.getInstance(entry.getKey()).validate(element);
                } catch (Exception e) { //some other, unhandled error occurred. Print what we can and die
                    errors.add(new CompilerError(element, e.getMessage(), entry.getKey()));
                }
            }
        }
        return new StepResult(errors);
    }
}
