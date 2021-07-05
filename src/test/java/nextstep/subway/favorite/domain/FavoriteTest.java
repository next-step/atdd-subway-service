package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {


    @Test
    void 즐겨찾기_객체_생성() {
        Long memberId = 1L;
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(3L, "역삼역");
        Favorite favorite = new Favorite(memberId, 강남역, 역삼역);
        assertThat(favorite).isNotNull();
        assertThat(favorite.getMemberId()).isEqualTo(memberId);
        assertThat(favorite.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favorite.getTarget().getId()).isEqualTo(역삼역.getId());
    }

    @Test
    void 즐겨찾기_정적_팩토리_메서드() {
        Long memberId = 1L;
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(3L, "역삼역");
        Favorite favorite = Favorite.create(memberId, 강남역, 역삼역);
        assertThat(favorite).isNotNull();
        assertThat(favorite.getMemberId()).isEqualTo(memberId);
        assertThat(favorite.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favorite.getTarget().getId()).isEqualTo(역삼역.getId());
    }
}
