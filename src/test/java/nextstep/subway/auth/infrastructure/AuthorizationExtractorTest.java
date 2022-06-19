package nextstep.subway.auth.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.auth.infrastructure.AuthorizationExtractor.AUTHORIZATION;
import static nextstep.subway.auth.infrastructure.AuthorizationExtractor.BEARER_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationExtractorTest {
    private static final String token = " Valid Token";
    private static final String value = BEARER_TYPE + token;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, value);

        httpServletRequest = request;
    }

    @Test
    void 요청_헤더에서_인증_토큰을_추출한다() {
        // when
        String result = AuthorizationExtractor.extract(httpServletRequest);

        // then
        assertThat(result).isEqualTo(token.trim());
    }
}
