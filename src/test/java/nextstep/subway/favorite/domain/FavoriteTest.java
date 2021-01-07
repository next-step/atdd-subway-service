package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create() {
        // given
        Station 강남역 = new Station("강남역");
        Station 정자역 = new Station("정자역");
        Long memberId = 1L;

        // when
        Favorite favorite = new Favorite(memberId, 강남역, 정자역);

        // then
        assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기를 추가할때 memberId, 출발역, 도착역 중 하나라도 없으면 생성할 수 없다.")
    @Test
    void createFailOfNull() {
        // given
        Station 강남역 = new Station("강남역");
        Station 정자역 = new Station("정자역");
        Long memberId = 1L;

        // when / then
        assertThrows(RuntimeException.class, () -> new Favorite(null, 강남역, 정자역));
        assertThrows(RuntimeException.class, () -> new Favorite(memberId, null, 정자역));
        assertThrows(RuntimeException.class, () -> new Favorite(memberId, 강남역, null));
    }

    @DisplayName("즐겨찾기의 출발역과 도착역은 같을 수 없다.")
    @Test
    void createFailSameStation() {
        // given
        Station 강남역 = new Station("강남역");
        Long memberId = 1L;

        // when / then
        assertThrows(IllegalArgumentException.class, () -> new Favorite(memberId, 강남역, 강남역));
    }
}
