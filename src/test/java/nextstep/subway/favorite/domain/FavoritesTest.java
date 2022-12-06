package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static nextstep.subway.favorite.domain.Favorites.FAVORITE_DUPLICATE_EXCEPTION_MESSAGE;
import static nextstep.subway.favorite.domain.Favorites.HAS_NOT_FAVORITE_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoritesTest {

    @DisplayName("출발역과 도착역이 같은 즐겨찾기를 생성할 수 없다.")
    @Test
    void add_fail_same() {
        Member member = new Member("email", "password", 20);
        Station stationA = new Station(1L, "A");
        Station stationB = new Station(2L, "B");

        Favorite favorite = new Favorite(member, stationA, stationB);

        Favorites favorites = new Favorites();
        favorites.add(favorite);

        assertThatThrownBy(() -> favorites.add(favorite))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(FAVORITE_DUPLICATE_EXCEPTION_MESSAGE);
    }

    @DisplayName("가지고 있지 않은 즐겨찾기를 삭제할 수 없다.")
    @Test
    void delete_fail() {
        Member member = new Member("email", "password", 20);
        Station stationA = new Station(1L, "A");
        Station stationB = new Station(2L, "B");

        Favorite favorite = new Favorite(member, stationA, stationB);

        Favorites favorites = new Favorites();

        assertThatThrownBy(() -> favorites.validateDeleteFavorite(favorite))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(HAS_NOT_FAVORITE_EXCEPTION_MESSAGE);
    }
}
