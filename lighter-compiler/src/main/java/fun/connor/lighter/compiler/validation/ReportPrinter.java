package fun.connor.lighter.compiler.validation;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class ReportPrinter {

    private final Messager messager;

    public ReportPrinter(final Messager messager) {
        this.messager = messager;
    }

    void printError(final ValidationError error) {
        if (!error.getLocationHint().isPresent()) {
            printErrorNoLocation(error);
        } else {
            LocationHint locationHint = error.getLocationHint().get();
            if (locationHint.getAnnotatioMirror().isPresent()) {
                printErrorAnnotationLocation(error, locationHint.getElement(), locationHint.getAnnotatioMirror().get());
            } else {
                printErrorElementLocation(error, locationHint.getElement());
            }
        }
    }

    private void printErrorNoLocation(final ValidationError error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error.toString());
    }

    private void printErrorElementLocation(final ValidationError error, Element hint) {
        System.out.println("using a location hint!");
        messager.printMessage(Diagnostic.Kind.ERROR, error.toString(), hint);
    }

    private void printErrorAnnotationLocation
            (final ValidationError error, Element element, AnnotationMirror annotationMirror) {
        messager.printMessage(Diagnostic.Kind.ERROR, error.toString(), element, annotationMirror);
    }
}
