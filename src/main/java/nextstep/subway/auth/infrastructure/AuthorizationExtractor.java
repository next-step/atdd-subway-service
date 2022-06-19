package nextstep.subway.auth.infrastructure;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static String BEARER_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
    public static final String COMMA = ",";
    public static final int FIRST_INDEX = 0;

    public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

        String authHeaderValue = null;
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            authHeaderValue = getHeaderValue(request, value);
        }
        return authHeaderValue;
    }

    private static String getHeaderValue(HttpServletRequest request, String value) {
        if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
            String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
            request.setAttribute(ACCESS_TOKEN_TYPE, BEARER_TYPE);

            return removeComma(authHeaderValue);
        }
        return null;
    }

    private static String removeComma(String authHeaderValue) {
        return authHeaderValue.split(COMMA)[FIRST_INDEX];
    }
}
