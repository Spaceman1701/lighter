package fun.connor.lighter.autoconfig;

/**
 * Runtime exception thrown in the case of any unrecoverable error
 * while attempting to load auto configuration objects.
 */
public class AutoConfigException extends RuntimeException {

    public AutoConfigException(String message) {
        super(message);
    }
}
