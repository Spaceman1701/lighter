package fun.connor.lighter.compiler.validation;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Prints formatted {@link ValidationReport} to the {@link Messager}. This object
 * should be used for printing any validation errors as it properly handles differing
 * location-hint specificity
 */
public class ReportPrinter {

    private final Messager messager;

    /**
     * @param messager The messager to print errors to
     */
    public ReportPrinter(final Messager messager) {
        this.messager = messager;
    }

    /**
     * print a single error to the messager. Use the most specific location hint possible
     * @param error the error to be printed
     */
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
