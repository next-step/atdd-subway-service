package nextstep.subway.utils;

import nextstep.subway.auth.dto.TokenResponse;

public final class AuthPrefixHelper {
    private static final String PREFIX = "Bearer ";

    private AuthPrefixHelper() {
    }

    public static String addAuthPrefix(TokenResponse tokenResponse) {
        if (tokenResponse == null) {
            return "";
        }
        return PREFIX + tokenResponse.getAccessToken();
    }
}
