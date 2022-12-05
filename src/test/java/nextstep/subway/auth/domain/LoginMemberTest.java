package nextstep.subway.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginMemberTest {

    @DisplayName("id, 이메일이 같으면 LoginMember는 같다.")
    @Test
    void equal(){
        LoginMember member1 = new LoginMember(1L, "yalmung@gmail.com", 20);
        LoginMember member2 = new LoginMember(1L, "yalmung@gmail.com", 20);

        Assertions.assertThat(member1).isEqualTo(member2);
    }

    @DisplayName("id가 다르면 LoginMember는 다르다.")
    @Test
    void notEqual1(){
        LoginMember member1 = new LoginMember(1L, "yalmung@gmail.com", 10);
        LoginMember member2 = new LoginMember(2L, "yalmung@gmail.com", 10);

        Assertions.assertThat(member1).isNotEqualTo(member2);
    }

    @DisplayName("이메일이 다르면 LoginMember는 동등하지 않다.")
    @Test
    void notEqual2(){
        LoginMember member1 = new LoginMember(1L, "yalmung@gmail.com", 10);
        LoginMember member2 = new LoginMember(1L, "yalmung2@gmail.com", 10);

        Assertions.assertThat(member1).isNotEqualTo(member2);
    }
}
