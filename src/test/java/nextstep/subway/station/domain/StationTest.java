package nextstep.subway.station.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {

    @DisplayName("지하철 역 이름이 같으면 두 지하철역은 동등하다.")
    @Test
    void equals() {
        Station station1 = new Station("강남역");
        Station station2 = new Station("강남역");

        Assertions.assertThat(station1).isEqualTo(station2);
    }

    @DisplayName("지하철 역 이름이 다르면 두 지하철역은 동등하지 않다..")
    @Test
    void notEquals() {
        Station station1 = new Station("강남역");
        Station station2 = new Station("광교역");

        Assertions.assertThat(station1).isNotEqualTo(station2);
    }
}
