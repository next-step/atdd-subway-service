package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    Station upStation;
    Station downStation;
    Line line;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행선");
        downStation = new Station("하행선");
        line = new Line("2호선", "green", upStation, downStation, 10);
    }

    @DisplayName("구간을 등록하고 노선의 역을 순서대로 찾는다")
    @Test
    void getStations() {
        Station newStation = new Station("신규역");
        line.addStation(newStation, upStation, 5);
        assertThat(line.getStations()).containsExactly(newStation, upStation, downStation);
    }

    @DisplayName("신규역을 상행선과 하행선 사이에 추가한다")
    @Test
    void addStation() {
        Station newStation = new Station("신규역");
        line.addStation(newStation, downStation, 5);
        assertThat(line.getStations()).containsExactly(upStation, newStation, downStation);
    }

    @DisplayName("구간을 삭제한다")
    @Test
    void removeStation() {
        Station newStation = new Station("신규역");
        line.addStation(newStation, upStation, 5);
        line.removeStation(upStation);
        assertThat(line.getStations()).containsExactly(newStation, downStation);
    }
}
