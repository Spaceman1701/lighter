package fun.connor.lighter.processor;

import com.google.auto.service.AutoService;
import fun.connor.lighter.declarative.*;
import fun.connor.lighter.processor.error.CompilerError;
import fun.connor.lighter.processor.processors.Controller;
import fun.connor.lighter.processor.step.CompilerStep;
import fun.connor.lighter.processor.step.ValidationStep;
import fun.connor.lighter.processor.validators.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;


@AutoService(Processor.class)
public class LighterAnnotationProcessor extends AbstractProcessor {


    private AnnotationValidatorDatabase annotationValidators;

    private List<CompilerStep> steps;

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        annotationValidators = new AnnotationValidatorDatabase();
        annotationValidators.registerValidator(ResourceController.class, new ResourceControllerValidator());
        annotationValidators.registerValidator(QueryParams.class, new QueryParamsValidator(env));
        annotationValidators.registerValidator(Body.class, new BodyValidator());

        annotationValidators.registerValidator(Get.class, new EndpointAnnotationValidator(env));
        annotationValidators.registerValidator(Delete.class, new EndpointAnnotationValidator(env));
        annotationValidators.registerValidator(Post.class, new EndpointAnnotationValidator(env));
        annotationValidators.registerValidator(Put.class, new EndpointAnnotationValidator(env));

        steps = new ArrayList<>();

        steps.add(new ValidationStep(env));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, Object> stepEnv = new HashMap<>();
//        Map<String, Set<? extends Element>> elementsByAnnotation = new HashMap<>();
//
//        for (TypeElement annotation : annotations) {
//            String annotationName = annotation.getQualifiedName().toString();
//            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
//            elementsByAnnotation.put(annotationName, elements);
//        }

        stepEnv.put("annotations", annotations);

        for (CompilerStep step : steps) {
            step.validateEnv(stepEnv);
            Set<CompilerError> stepErrors = step.process(roundEnv);
            if (!stepErrors.isEmpty()) {
                for (CompilerError error : stepErrors) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error.toString());
                }
                return true;
            }
        }


//        Set<CompilerError> validationErrors = validateAnnotationSet(elementsByAnnotation);
//        if (!validationErrors.isEmpty()) {
//            for (CompilerError error : validationErrors) {
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, error.toString());
//            }
//            return true;
//        }



        return false;
    }

    private Set<CompilerError> parseAnnotations(RoundEnvironment roundEnv) {
        TypeElement responseType =
                processingEnv.getElementUtils().getTypeElement(ResourceController.class.getCanonicalName());

        Set<? extends Element> controllerElements = roundEnv.getElementsAnnotatedWith(responseType);

        Set<Controller> controllers = new HashSet<>();

        for (Element e : controllerElements) {
            TypeElement type = (TypeElement) controllerElements;
            Controller controller = new Controller(processingEnv, roundEnv, type);
            controllers.add(controller);
        }

        return null;
    }

    private Set<CompilerError> validateAnnotationSet
            (Map<String, Set<? extends Element>> elementsByAnnotation) {
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
        return errors;
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
