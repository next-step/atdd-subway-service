package nextstep.subway.auth.infrastructure;

import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    protected AuthorizationExtractor() {
    }

    public static Optional<String> extract(HttpServletRequest request) {
        return Collections.list(request.getHeaders(AUTHORIZATION))
                .stream()
                .filter(AuthorizationExtractor::isBearerTypePrefix)
                .findFirst()
                .map(value -> getToken(request, value));
    }

    private static String getToken(HttpServletRequest request, String value) {
        request.setAttribute(ACCESS_TOKEN_TYPE, getBearerTypePrefix(value));
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
        int commaIndex = authHeaderValue.indexOf(",");
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }

    private static boolean isBearerTypePrefix(String value) {
        return value.toLowerCase()
                .startsWith(BEARER_TYPE.toLowerCase());
    }

    private static String getBearerTypePrefix(String value) {
        return value.substring(0, BEARER_TYPE.length()).trim();
    }
}
