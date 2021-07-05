package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteResponseTest {


    @Test
    void favorite_객체를_이용하여_FavoriteResponse_객체_생성() {
        Long memberId = 1L;
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(3L, "역삼역");
        FavoriteResponse favoriteResponse = FavoriteResponse.of(new Favorite(memberId, 강남역, 역삼역));
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(역삼역.getId());
    }
}
