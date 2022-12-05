package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoritesTest {
    @Test
    @DisplayName("즐겨찾기 목록 생성")
    void createFavorites() {
        Favorites actual = Favorites.from(Collections.singletonList(
                Favorite.of(
                        1L,
                        Station.from("강남역"),
                        Station.from("잠실역")
                )));

        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Favorites.class)
        );
    }
}
