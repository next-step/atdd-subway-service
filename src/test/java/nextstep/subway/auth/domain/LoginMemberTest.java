package nextstep.subway.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("생성된 LoginMember 테스트")
public class LoginMemberTest {

    @DisplayName("id, 이메일이 같으면 동등")
    @Test
    void whenIdAndEmailSameThenEquals(){
        LoginMember member1 = new LoginMember(1L, "a@gmail.com", 10);
        LoginMember member2 = new LoginMember(1L, "a@gmail.com", 10);

        Assertions.assertThat(member1).isEqualTo(member2);
    }

    @DisplayName("id가 다르면 LoginMember는 동등하지 않음")
    @Test
    void whenIdIsDifferentThenNotEquals(){
        LoginMember member1 = new LoginMember(1L, "a@gmail.com", 10);
        LoginMember member2 = new LoginMember(2L, "a@gmail.com", 10);

        Assertions.assertThat(member1).isNotEqualTo(member2);
    }

    @DisplayName("이메일이 다르면 LoginMember는 동등하지 않음")
    @Test
    void whenEmailIsDifferentThenNotEquals(){
        LoginMember member1 = new LoginMember(1L, "a@gmail.com", 10);
        LoginMember member2 = new LoginMember(1L, "b@gmail.com", 10);

        Assertions.assertThat(member1).isNotEqualTo(member2);
    }
}
