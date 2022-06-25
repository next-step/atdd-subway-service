package nextstep.subway.auth.infrastructure;

public interface TokenProvider {
    String createToken(final String payload);

    String getPayload(final String token);

    boolean validateToken(final String token);
}
