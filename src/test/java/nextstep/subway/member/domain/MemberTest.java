package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("Member 는 이메일, 비밀번호, 나이 정보로 생성할 수 있다.")
    @Test
    void create1() {
        // when & then
        assertThatNoException().isThrownBy(() -> Member.of(EMAIL, PASSWORD, AGE));
    }

    @DisplayName("Member 생성 시, 이메일이 null or 빈 문자열이면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create2(String email) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Member.of(email, PASSWORD, AGE));
    }

    @DisplayName("Member 생성 시, 비밀번호가 null or 빈 문자열이면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create3(String password) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Member.of(EMAIL, password, AGE));
    }

    @DisplayName("Member 생성 시, 나이가 null 이면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create4(Integer age) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Member.of(EMAIL, PASSWORD, age));
    }

    @DisplayName("Member 생성 시, 나이가 0 이거나 음수면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -10})
    void create5(Integer age) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Member.of(EMAIL, PASSWORD, age));
    }

    @DisplayName("Member 의 이메일, 비밀번호, 나이를 수정한다.")
    @Test
    void update1() {
        // given
        Member member = Member.of(EMAIL, PASSWORD, AGE);

        // when
        String newEmail = "test@test.com";
        String newPassword = "newPassword";
        int newAge = 100;

        member.update(Member.of(newEmail, newPassword, newAge));

        // then
        assertAll(
            () -> assertThat(member.getEmail()).isEqualTo(newEmail),
            () -> assertThat(member.getPassword()).isEqualTo(newPassword),
            () -> assertThat(member.getAge()).isEqualTo(newAge)
        );
    }

    @DisplayName("이메일이 null or 빈문자열이면 Member 수정 시 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void update2(String newEmail) {
        // given
        Member member = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        String newPassword = "newPassword";
        int newAge = 100;

        assertThatIllegalArgumentException().isThrownBy(() -> member.update(Member.of(newEmail, newPassword, newAge)));
    }

    @DisplayName("비밀번호가 null or 빈문자열이면 Member 수정 시 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void update3(String newPassword) {
        // given
        Member member = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        String newEmail = "test@test.com";
        int newAge = 100;

        assertThatIllegalArgumentException().isThrownBy(() -> member.update(Member.of(newEmail, newPassword, newAge)));
    }

    @DisplayName("나이가 null 이면 Member 수정 시 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void update4(Integer newAge) {
        // given
        Member member = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        String newEmail = "test@test.com";
        String newPassword = "newPassword";

        assertThatIllegalArgumentException().isThrownBy(() -> member.update(Member.of(newEmail, newPassword, newAge)));
    }

    @DisplayName("나이가 0 이거나 음수면 Member 수정 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -10})
    void update5(Integer newAge) {
        // given
        Member member = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        String newEmail = "test@test.com";
        String newPassword = "newPassword";

        assertThatIllegalArgumentException().isThrownBy(() -> member.update(Member.of(newEmail, newPassword, newAge)));
    }
}