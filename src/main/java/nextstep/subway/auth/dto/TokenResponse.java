package nextstep.subway.auth.dto;

import java.util.Objects;

public class TokenResponse {
    private final String accessToken;

    private TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse of(final String accessToken) {
        return new TokenResponse(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TokenResponse that = (TokenResponse) o;
        return Objects.equals(accessToken, that.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken);
    }
}
