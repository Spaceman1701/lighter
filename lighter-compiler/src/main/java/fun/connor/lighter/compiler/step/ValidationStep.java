package fun.connor.lighter.compiler.step;

import fun.connor.lighter.compiler.validation.LocationHint;
import fun.connor.lighter.compiler.validation.ValidationReport;
import fun.connor.lighter.compiler.validators.AnnotationValidator;
import fun.connor.lighter.compiler.validators.AnnotationValidatorMap;
import fun.connor.lighter.compiler.validators.EndpointAnnotationValidator;
import fun.connor.lighter.compiler.validators.ResourceControllerValidator;
import fun.connor.lighter.declarative.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Validates that annotations are placed correctly and have legal argument values.
 */
public class ValidationStep extends CompilerStep {


    private Set<? extends TypeElement> annotations;

    private AnnotationValidatorMap validatorMap;

    public ValidationStep(ProcessingEnvironment env) {
        super(env);
        validatorMap = new AnnotationValidatorMap();

        validatorMap.register(ResourceController.class, ResourceControllerValidator::new);
        validatorMap.register(Post.class, EndpointAnnotationValidator::new);
        validatorMap.register(Get.class, EndpointAnnotationValidator::new);
        validatorMap.register(Put.class, EndpointAnnotationValidator::new);
        validatorMap.register(Delete.class, EndpointAnnotationValidator::new);
    }

    @Override
    @SuppressWarnings("unchecked") //TODO: resolve generic type issues
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

    @Override @SuppressWarnings("unchecked")
    public StepResult process(RoundEnvironment roundEnv) {

        Map<Class<? extends Annotation>, Set<? extends Element>> elementsByAnnotation = new HashMap<>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (TypeElement annotation : annotations) {
            String annotationName = annotation.getQualifiedName().toString();
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);

            try {
                elementsByAnnotation.put((Class<? extends Annotation>) classLoader.loadClass(annotationName), elements);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("something went very, very, very wrong", e);
            }
        }




        return new StepResult(doValidation(elementsByAnnotation));
    }


    private ValidationReport doValidation(Map<Class<? extends Annotation>, Set<? extends Element>> elementsByAnnotation) {
        ValidationReport.Builder validationReportBuilder = ValidationReport.builder();

        for (Map.Entry<Class<? extends Annotation>, Set<? extends Element>> entry : elementsByAnnotation.entrySet()) {
            Class<? extends Annotation> annotationClass = entry.getKey();
            for (Element e : entry.getValue()) {
                validateSingleElement(e, annotationClass, validationReportBuilder);
            }
        }

        return validationReportBuilder.build();
    }

    private void validateSingleElement
            (Element annotatedElement, Class<? extends Annotation> clazz, ValidationReport.Builder report)
    {
        Annotation annotation = annotatedElement.getAnnotation(clazz);
        AnnotationValidator validator = validatorMap.getAnnotationValidator(annotatedElement, annotation, clazz);
        if (validator != null) {
            AnnotationMirror annotationMirror = types.getAnnotationMirror(annotatedElement, clazz);
            LocationHint locationHint = new LocationHint(annotatedElement, annotationMirror);
            ValidationReport.Builder annotationReport = ValidationReport.builder(locationHint);
            validator.validate(annotationReport);

            report.addChild(annotationReport);
        }
    }
}
