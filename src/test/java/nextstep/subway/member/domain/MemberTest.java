package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MemberTest {
    @Test
    @DisplayName("회원 객체 생성")
    void createMember() {
        // when
        Member actual = Member.of("email@email.com", "password", 20);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Member.class)
        );
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() {
        // given
        Member actual = Member.of("email@email.com", "password", 20);

        // when
        actual.update(Member.of("update@email.com", "update", 21));

        // then
        assertAll(
                () -> assertThat(actual.emailValue()).isEqualTo("update@email.com"),
                () -> assertThat(actual.getPassword()).isEqualTo("update"),
                () -> assertThat(actual.getAge()).isEqualTo(21)
        );
    }
}
