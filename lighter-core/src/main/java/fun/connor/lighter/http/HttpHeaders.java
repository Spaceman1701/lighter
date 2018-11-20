package fun.connor.lighter.http;

import java.util.Objects;

/**
 * Common HTTP 1.0, 1.1, and 2.0 header tags. See associated RFCs.
 */
public final class HttpHeaders {

    public static final String ACCEPT = "Accept";

    public static final String ACCEPT_CHARSET = "Accept-Charset";

    public static final String ACCEPT_DATETIME = "Accept-Datetime";

    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    public static final String ACCEPT_PATCH = "Accept-Patch";

    public static final String ACCEPT_RANGES = "Accept-Ranges";

    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    public static final String ACCESS_CONTROL_ALLOW_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    public static final String AGE = "Age";

    public static final String ALLOW = "Allow";

    public static final String ALT_SVC = "Alt-Svc";

    public static final String AUTHORIZATION = "Authorization";

    public static final String A_IM = "A_IM";

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String CONNECTION = "Connection";

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_LOCATION = "Content-Location";

    public static final String CONTENT_MD5 = "Content-MD5";

    public static final String CONTENT_RANGE = "Content-Range";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String COOKIE = "Cookie";

    public static final String DELTA_BASE = "Delta-Base";

    public static final String Date = "Date";

    public static final String ETAG = "ETag";

    public static final String EXCEPT = "Except";

    public static final String EXPIRES = "Expires";

    public static final String FORWARDED = "Forwarded";

    public static final String FROM = "From";

    public static final String HOST = "Host";

    public static final String IF_MATCHED = "If-Matched";

    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    public static final String IF_NONE_MATCH = "If-None-Match";

    public static final String IF_RANGE = "If-Range";

    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    public static final String IM = "IM";

    public static final String LAST_MODIFIED = "Last-Modified";

    public static final String LINK = "Link";

    public static final String LOCATION = "Location";

    public static final String MAX_FORWARDS = "Max-Forwards";

    public static final String ORIGIN = "Origin";

    public static final String P3P = "P3P";

    public static final String PRAGMA = "Pragma";

    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";

    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

    public static final String PUBLIC_KEY_PINS = "Public-Key-Pins";

    public static final String RANGE = "Range";

    public static final String REFERER = "Referer";

    public static final String RETRY_AFTER = "Retry-After";

    public static final String SERVER = "Server";

    public static final String SET_COOKIE = "Set-Cookie";

    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    public static final String TE = "TE";

    public static final String TK = "Tk";

    public static final String TRAILER = "Trailer";

    public static final String TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String UPGRADE = "Upgrade";

    public static final String USER_AGENT = "User-Agent";

    public static final String VARY = "Vary";

    public static final String VIA = "Via";

    public static final String WARNING = "Warning";

    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    public static final String X_FRAME_OPTIONS = "X-Frame-Options";


    public static boolean equal(String a, String b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.equalsIgnoreCase(b); //faster method using tree for known headers is possible
    }

    private HttpHeaders() {}
}
