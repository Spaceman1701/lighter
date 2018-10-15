package fun.connor.lighter.processor;

import com.google.auto.service.AutoService;
import fun.connor.lighter.declarative.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;


@AutoService(Processor.class)
public class LighterAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "this is a test error");
        return true;
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
