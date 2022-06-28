package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 광교중앙역 = new Station("광교중앙역");

    @DisplayName("즐겨찾기 객체 정상 생성")
    @Test
    void constructor() {
        Favorite favorite = new Favorite(강남역, 광교중앙역, 1L);
        assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기 객체 생성시 null 매개변수 입력")
    @Test
    void constructorWithNull() {
        assertThatThrownBy(() -> new Favorite(강남역, null, 1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("즐겨찾기 객체 생성시 출발역과 도착역 동일")
    @Test
    void constructorSameStation() {
        assertThatThrownBy(() -> new Favorite(강남역, 강남역, 1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isSameRoute() {
        Favorite favorite = new Favorite(강남역, 광교중앙역, 1L);
        Favorite reverse = new Favorite(광교중앙역, 강남역, 1L);
        assertThat(favorite.isSameRoute(favorite)).isTrue();
        assertThat(favorite.isSameRoute(reverse)).isFalse();
    }
}
