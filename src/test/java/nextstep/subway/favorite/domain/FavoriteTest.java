package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class FavoriteTest {
    private Station 강남역;
    private Station 양재역;
    private long ownerId;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        // given
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        ownerId = 1L;
        즐겨찾기 = new Favorite(강남역, 양재역, ownerId);
    }

    @Test
    void isOwnedBy() {
        // when, then
        assertThat(즐겨찾기.isOwnedBy(ownerId)).isTrue();
        assertThat(즐겨찾기.isOwnedBy(2L)).isFalse();
    }
}
