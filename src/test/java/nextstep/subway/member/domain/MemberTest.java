package nextstep.subway.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 도메인 관련")
class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("test@domain.com", "randomPassword!!@#", 31);
    }

    @DisplayName("회원을 수정할 수 있다")
    @Test
    void updateMember() {
        Member updatedMember = new Member("changed@change.com", "newPw123908@dfklaj", 23);
        member.update(updatedMember);
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo("changed@change.com"),
                () -> assertThat(member.getPassword()).isEqualTo("newPw123908@dfklaj"),
                () -> assertThat(member.getAge()).isEqualTo(23)
        );
    }
}