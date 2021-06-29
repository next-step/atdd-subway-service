package nextstep.subway.auth.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "12345678901234567890123456789012";
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
    }

    @Test
    void createToken() {
        String givenPayload = "test@test.com";

        String actual = jwtTokenProvider.createToken(givenPayload);

        assertThat(actual).isNotNull();
    }
}
