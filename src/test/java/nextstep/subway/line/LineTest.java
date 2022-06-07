package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 단위테스트")
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

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addStation2() {
        Station newStation1 = new Station("신규역1");
        Station newStation2 = new Station("신규역2");
        line.addStation(upStation, newStation1, 2);
        line.addStation(newStation2, upStation, 5);
        assertThat(line.getStations()).containsExactly(newStation2, upStation, newStation1, downStation);
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addStationWithSameStation() {
        assertThatThrownBy(() -> line.addStation(upStation, downStation, 2))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addStationWithNoStation() {
        Station newStation1 = new Station("신규역1");
        Station newStation2 = new Station("신규역2");
        assertThatThrownBy(() -> line.addStation(newStation1, newStation2, 2))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeStation2() {
        assertThatThrownBy(() -> line.removeStation(upStation))
                .isInstanceOf(RuntimeException.class);
    }
}
