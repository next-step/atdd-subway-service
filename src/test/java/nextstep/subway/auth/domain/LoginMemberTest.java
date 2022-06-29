package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.path.domain.AgeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoginMemberTest {


    @DisplayName("청소년인지 확인한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void teenAger(int age) {
        //given
        LoginMember loginMember = new LoginMember(1L, "TEST@gamil.com", age);

        //when & Then
        assertThat(loginMember.getAgeType()).isEqualTo(AgeType.TEENAGER);
    }

    @DisplayName("어린이인지 확인한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void children(int age) {
        //given
        LoginMember loginMember = new LoginMember(1L, "TEST@gamil.com", age);

        //when & Then
        assertThat(loginMember.getAgeType()).isEqualTo(AgeType.CHILDREN);
    }

    @DisplayName("범위 이외의 나이는 None 이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 20, 21, 4})
    void isNone(int age) {
        //given
        LoginMember loginMember = new LoginMember(1L, "TEST@gamil.com", age);

        //when & Then
        assertThat(loginMember.getAgeType()).isEqualTo(AgeType.NONE);
    }

    @DisplayName("비회원인 확인")
    @Test
    void isGuest() {
        //given
        LoginMember loginMember = LoginMember.guestLogin();

        //when & Then
        assertThat(loginMember.isGuest()).isTrue();
    }

    @DisplayName("비회원인경우에는 AgeType이 None이다")
    @Test
    void isGuestIsAgeNone() {
        //given
        LoginMember loginMember = LoginMember.guestLogin();

        //when & Then
        assertThat(loginMember.getAgeType()).isEqualTo(AgeType.NONE);
    }
}