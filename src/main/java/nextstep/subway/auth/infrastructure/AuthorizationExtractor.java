package nextstep.subway.auth.infrastructure;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class AuthorizationExtractor {
    private AuthorizationExtractor() {
    }

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
    public static final char COMMA = ',';
    private static final int ZERO = 0;

    public static String extract(HttpServletRequest request) {
        final Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            final String header = headers.nextElement();
            final String authHeaderValue = getAuthTokenFromHeader(header);
            if (authHeaderValue != null) {
                setAccessTokenAtRequest(request, authHeaderValue);
                return authHeaderValue;
            }
        }
        return null;
    }

    private static void setAccessTokenAtRequest(HttpServletRequest request, String authHeaderValue) {
        request.setAttribute(ACCESS_TOKEN_TYPE, authHeaderValue.trim());
    }

    private static String getAuthTokenFromHeader(String value) {
        if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
            String authToken = value.substring(BEARER_TYPE.length()).trim();
            int commaIndex = authToken.indexOf(COMMA);
            if (commaIndex > ZERO) {
                authToken = authToken.substring(ZERO, commaIndex);
            }
            return authToken;
        }
        return null;
    }
}
