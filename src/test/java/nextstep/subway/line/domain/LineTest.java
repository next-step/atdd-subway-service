package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @DisplayName("역이 등록되어 있다면, 등록된 역을 조회한다.")
    @Test
    void getStationsWithStation() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line line = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);

        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations()).contains(강남역, 광교역);
    }

    @DisplayName("역이 등록되어 있지 않다면, 빈 리스트를 반환한다.")
    @Test
    void getStationsWithoutStation() {
        Line line = new Line("신분당선", "bg-red-600");

        assertThat(line.getStations()).hasSize(0);
    }
}
