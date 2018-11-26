package fun.connor.lighter.compiler.validation;

/**
 * Interface for objects which are capable of being validated
 */
public interface Validatable {

    /**
     * Validate that this instance conforms to a set of invariants. Any
     * errors should be added to the reportBuilder. Child classes of the instance
     * object should have their own sub-reports.
     * @param reportBuilder the report to write errors and sub-reports to
     */
    void validate(ValidationReport.Builder reportBuilder);
}
