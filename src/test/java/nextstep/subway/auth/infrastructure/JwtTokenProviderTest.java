package nextstep.subway.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("토큰을 생성할 수 있다.")
    @Test
    void createToken() {
        //given
        String payload = "email@sixthou.kro.kr";
        //when
        String token = jwtTokenProvider.createToken(payload);
        //then
        assertThat(token).isNotNull();
    }

    @DisplayName("토큰으로 이메일 주소를 찾을 수 있다.")
    @Test
    void getPayload() {
        //given
        String email = "email@sixthou.kro.kr";
        String token = jwtTokenProvider.createToken(email);
        //when
        String actual = jwtTokenProvider.getPayload(token);
        //then
        assertThat(email).isEqualTo(actual);
    }

    @DisplayName("비정상 토큰일 경우 validate 결과 값은 false 이다.")
    @Test
    void validateToken() {
        //given
        String token = "temp_token_string";

        //when
        assertThat(jwtTokenProvider.validateToken(token)).isFalse();

    }
}
