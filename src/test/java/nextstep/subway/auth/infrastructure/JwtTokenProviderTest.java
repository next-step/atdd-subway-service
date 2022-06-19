package nextstep.subway.auth.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private static String secretKey = "E1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLW";
    private static long validityInMilliseconds = 6000L;

    private JwtTokenProvider jwtTokenProvider;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);
        jwtToken = jwtTokenProvider.createToken(EMAIL);
    }

    @Test
    void 토큰의_유효성을_검사한다() {
        // when
        boolean result = jwtTokenProvider.validateToken(jwtToken);

        // then
        assertThat(result).isTrue();
    }
    
    @Test
    void 토큰의_페이로드를_조회한다() {
        // when
        String result = jwtTokenProvider.getPayload(jwtToken);

        // then
        assertThat(result).isEqualTo(EMAIL);
    }
}
