package nextstep.subway.member.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {
    private String EMAIL = "email@email.com";
    private String PASSWORD = "password";
    private int AGE = 10;
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");

    @DisplayName("사용자의 비밀번호와 다른 비밀번호를 입력받으면 AuthorizationException 발생한다.")
    @Test
    void checkPassword_exception() {
        // given
        Member member = new Member(EMAIL, PASSWORD ,AGE);
        String wrongPassword = "wrongPassword";
        // then
        assertThatThrownBy(() ->member.checkPassword(wrongPassword)).isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("회원의 즐겨찾기를 추가할 수 있다.")
    @Test
    void addFavorite_test() {
        // given
        Member member = new Member(EMAIL, PASSWORD ,AGE);
        // when
        member.addFavorite(강남역, 양재역);
        // then
        assertTrue(member.getFavorites().contains(Favorite.of(member, 강남역, 양재역)));
    }

}
