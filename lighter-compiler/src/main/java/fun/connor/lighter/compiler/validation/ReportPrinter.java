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

    void printError(final String prefix, final ValidationError error) {
        if (!error.getLocationHint().isPresent()) {
            printErrorNoLocation(prefix, error);
        } else {
            LocationHint locationHint = error.getLocationHint().get();
            if (locationHint.getAnnotatioMirror().isPresent()) {
                printErrorAnnotationLocation(prefix, error, locationHint.getElement(), locationHint.getAnnotatioMirror().get());
            } else {
                printErrorElementLocation(prefix, error, locationHint.getElement());
            }
        }
    }

    void printContextMessage(final String prefix, final String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, prefix + message);
    }

    private void printErrorNoLocation(final String prefix, final ValidationError error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error.toStringRelative(prefix));
    }

    private void printErrorElementLocation(final String prefix, final ValidationError error, Element hint) {
        System.out.println("using a location hint!");
        messager.printMessage(Diagnostic.Kind.ERROR, error.toStringRelative(prefix), hint);
    }

    private void printErrorAnnotationLocation
            (final String prefix, final ValidationError error, Element element, AnnotationMirror annotationMirror) {
        messager.printMessage(Diagnostic.Kind.ERROR, error.toStringRelative(prefix), element, annotationMirror);
    }
}
