package fun.connor.lighter.handler;

import java.util.List;

/**
 * An HTTP request. Lighter backend implementations should provide
 * an implementation of this interface. Implementations of this interface
 * need not be thread safe (unless required by the Lighter backend). For optimization,
 * backends can choose to lazily or eagerly load data for this interface. Applications
 * should not be expected to interact with implementations of this interface.
 */
public interface Request {

    /**
     * Get an HTTP header from the request
     * @param header the header name to read
     * @return The first header value as a string or null if the header is not present
     */
    String getHeaderValue(String header);

    /**
     * Get an HTTP header from the request
     * @param header the header name to read
     * @return The header values as a string or null if the header is not present
     */
    List<String> getHeaderValues(String header);

    /**
     * Get the IANA Media type for this request if it is present.
     * @return the IANA Media type as a string or null if there is none
     */
    String getContentType();

    /**
     * The path or URI of the request
     * @return the actual request path as a String
     */
    String getRequestPath();

    /**
     * @return the HTTP method of this request
     */
    String getMethod();

    /**
     * Gets the entire request body as a String. Future versions of this
     * interface will provide methods for reading the body as a stream
     * for improved performance.
     * @return the request body as a UTF-8 string.
     */
    String getBody();
}
