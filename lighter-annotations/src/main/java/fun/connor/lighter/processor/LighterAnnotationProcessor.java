package fun.connor.lighter.processor;

import com.google.auto.service.AutoService;
import fun.connor.lighter.declarative.*;
import fun.connor.lighter.processor.validators.AnnotationValidatorDatabase;
import fun.connor.lighter.processor.validators.ResourceControllerValidator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@AutoService(Processor.class)
public class LighterAnnotationProcessor extends AbstractProcessor {


    private AnnotationValidatorDatabase annotationValidators;

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        annotationValidators = new AnnotationValidatorDatabase();
        annotationValidators.registerValidator(ResourceController.class, new ResourceControllerValidator());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, Set<? extends Element>> elementsByAnnotation = new HashMap<>();

        for (TypeElement annotation : annotations) {
            String annotationName = annotation.getQualifiedName().toString();
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            elementsByAnnotation.put(annotationName, elements);
        }


        if (!validateAnnotationSet(elementsByAnnotation, processingEnv.getMessager())) {
            return true;
        }



        return false;
    }

    private boolean validateAnnotationSet(Map<String, Set<? extends Element>> elementsByAnnotation, Messager messager) {
        boolean hadErrors = false;
        for (Map.Entry<String, Set<? extends Element>> entry : elementsByAnnotation.entrySet()) {
            for (Element element : entry.getValue()) {
                try {
                    annotationValidators.getInstance(entry.getKey()).validate(element);
                } catch (IllegalArgumentException e) {
                    hadErrors = true;
                    handleValidationError(entry.getKey(), element, e, messager);
                } catch (Exception e) { //some other, unhandled error occurred. Print what we can and die
                    hadErrors = true;
                    handleUnknownValidationError(entry.getKey(), element, e, messager);
                }
            }
        }

        return hadErrors;
    }

    private void handleUnknownValidationError
            (String annotationName, Element element, Exception e, Messager messager) {

        String elementStr = elementKindToErrorString(element.getKind());
        String message = "unknown error processing annotated " + elementStr + " " + element.getSimpleName()
                + " (annotation: " + annotationName + "): " + e.getMessage();


        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void handleValidationError
            (String annotationName, Element element, IllegalArgumentException e, Messager messager) {
        String elementStr = elementKindToErrorString(element.getKind());
        String message = "error processing annotated " + elementStr + " " + element.getSimpleName()
                + " (annotation: " + annotationName + "): " + e.getMessage();

        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private String elementKindToErrorString(ElementKind kind) {
        switch (kind) {
            case CLASS: return "class";
            case METHOD: return "method";
            case PARAMETER: return "parameter";
            default: return "element";
        }
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
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
