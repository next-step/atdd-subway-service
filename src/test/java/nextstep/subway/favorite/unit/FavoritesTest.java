package nextstep.subway.favorite.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoritesTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 양재역 = new Station("양재역");
    private final Member 회원 = new Member("email@email.com", "password", 10);


    @DisplayName("즐겨찾기 목록에 즐겨찾기를 추가할 수 있다.")
    @Test
    void add_test() {
        // given
        Favorites 즐겨찾기_목록 = new Favorites();
        Favorite 즐겨찾기 = Favorite.of(회원, 강남역, 양재역);
        // when
        즐겨찾기_목록.add(즐겨찾기);
        // then
        assertTrue(즐겨찾기_목록.contains(즐겨찾기));
    }


    @DisplayName("이미 존재하는 즐겨찾기를 추가할때 IllegalStateException 발생한다.")
    @Test
    void add_exception() {
        // given
        Favorites 즐겨찾기_목록 = new Favorites();
        Favorite 즐겨찾기 = Favorite.of(회원, 강남역, 양재역);
        즐겨찾기_목록.add(즐겨찾기);

        // then
        assertThatThrownBy(() ->즐겨찾기_목록.add(즐겨찾기)).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("존재하지 않는 즐겨찾기를 삭제 할때 IllegalStateException 발생한다.")
    @Test
    void remove_exception() {
        // given
        Favorites 즐겨찾기_목록 = new Favorites();
        Favorite 즐겨찾기 = Favorite.of(회원, 강남역, 양재역);
        // then
        assertThatThrownBy(() ->즐겨찾기_목록.remove(즐겨찾기)).isInstanceOf(IllegalStateException.class);
    }


}
