package nextstep.subway.favorite.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.favorite.domain.Favorite.NOT_OWNER_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @DisplayName("로그인한 유저가 다른 즐겨찾기를 삭제할 경우 예외를 발생시킨다.")
    @Test
    void checkOwnerException() {
        //given
        Member member = new Member("email", "password", 20);
        ReflectionTestUtils.setField(member, "id", 1L);
        Favorite favorite = new Favorite(null, null, member);
        LoginMember loginMember = new LoginMember(99L, "email99", 99);

        //when
        assertThatThrownBy(() -> favorite.checkOwner(loginMember))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(NOT_OWNER_EXCEPTION_MESSAGE);
    }
}
