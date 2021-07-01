package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @Test
    void create() {
        Station station1 = new Station("서울역");
        Station station2 = new Station("강남역");
        long loginMemberId = 1L;
        Favorite favorite = new Favorite(loginMemberId, station1, station2);

        assertThat(favorite).isNotNull();
    }
}
