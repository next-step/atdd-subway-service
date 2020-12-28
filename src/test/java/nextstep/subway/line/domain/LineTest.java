package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @DisplayName("등록된 구간의 첫번째 역을 찾을 수 있다.")
    @Test
    void findFirstStationTest() {
        String name = "2호선";
        String color = "초록색";
        int distance = 3;
        Station upStation = new Station("강남역");
        Station downStation = new Station("서초역");
        Line line = new Line(name, color, upStation, downStation, distance);

        Station firstStation =  line.findUpStation();

        assertThat(firstStation.getName()).isEqualTo(upStation.getName());
    }
}