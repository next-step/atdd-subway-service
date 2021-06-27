package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 도메인 클래스 테스트")
public class FavoriteTest {

    private Long id = 1L;
    private String stationName1 = "역1";
    private String stationName2 = "역2";

    @Test
    void create() {
        Favorite favorite = new Favorite(id, Station.of(stationName1), Station.of(stationName2));

        assertThat(favorite.getId()).isEqualTo(id);
        assertThat(favorite.getSource().getName()).isEqualTo(stationName1);
        assertThat(favorite.getTarget().getName()).isEqualTo(stationName2);
    }

}
