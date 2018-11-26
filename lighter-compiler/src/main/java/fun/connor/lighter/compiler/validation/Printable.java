package fun.connor.lighter.compiler.validation;

/**
 * Printable objects can be properly printed to the compiler log
 */
public interface Printable {

    /**
     * Print this object using the provided {@link ReportPrinter}
     * @param printer the report printer to use
     */
    void print(ReportPrinter printer);
}
