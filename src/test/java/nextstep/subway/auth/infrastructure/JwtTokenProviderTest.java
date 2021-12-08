package nextstep.subway.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.auth.infrastructure
 * fileName : JwtTokenProviderTest
 * author : haedoang
 * date : 2021-12-06
 * description :
 */
@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("jwt 토큰을 생성한다.")
    public void create() throws Exception {
        // given
        String given = "haedoang@gmail.com";

        // when
        String token = jwtTokenProvider.createToken(given);
        String payload = jwtTokenProvider.getPayload(token);

        // then
        assertThat(payload).isEqualTo(given);

        // when
        boolean tokenResult = jwtTokenProvider.validateToken(token);

        // then
        assertThat(tokenResult).isTrue();
    }
}
