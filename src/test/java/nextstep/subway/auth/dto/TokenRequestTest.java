package nextstep.subway.auth.dto;

import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenRequestTest {


    @Test
    void 회원_정보를_이용하여_TokenRequest_생성() {
        Member member = new Member("mwkwon@test.com", "password", 37);
        TokenRequest tokenRequest = TokenRequest.of(member);
        assertThat(tokenRequest.getEmail()).isEqualTo("mwkwon@test.com");
        assertThat(tokenRequest.getPassword()).isEqualTo("password");
    }
}
