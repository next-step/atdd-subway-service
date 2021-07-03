package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.exception.AuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IncompleteLoginMemberTest {

    @DisplayName("로그인의 유무를 반환한다.")
    @Test
    void isCompleteLoginMember() {
        //given
        LoginMember loginMember = new LoginMember(1L, "email@nextstep.com", 30);
        IncompleteLoginMember incompleteLoginMember = new IncompleteLoginMember(loginMember);

        //when
        boolean actual = incompleteLoginMember.isCompleteLoginMember();

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("로그인한 유저는 로그인 정보를 반환한다.")
    @Test
    void toCompleteLoginMember() {
        //given
        LoginMember loginMember = new LoginMember(1L, "email@nextstep.com", 30);
        IncompleteLoginMember incompleteLoginMember = new IncompleteLoginMember(loginMember);

        //when
        LoginMember actual = incompleteLoginMember.toCompleteLoginMember();

        //then
        assertThat(actual).isEqualTo(loginMember);
    }

    @DisplayName("로그인하지 않은 비회원 유저가 로그인 정보를 반환하려하면 예외를 발생시킨다.")
    @Test
    void toCompleteLoginMemberException() {
        //given
        IncompleteLoginMember incompleteLoginMember = IncompleteLoginMember.ofNull();

        //when
        assertThatThrownBy(incompleteLoginMember::toCompleteLoginMember)
                .isInstanceOf(AuthorizationException.class); //then
    }
}
