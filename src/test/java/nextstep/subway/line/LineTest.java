package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 관련 기능 테스트")
public class LineTest {
    private final Station upStation = new Station("소사역");
    private final Station downStation = new Station("역곡역");

    @Test
    @DisplayName("노선을 생성한다.")
    void createLine() {
        Line line = LineTestFixture.노선을_생성한다("1호선", "red", upStation, downStation, 10, 100);

        assertThat(line.getName()).isEqualTo("1호선");
        assertThat(line.getColor()).isEqualTo("red");
    }

    @Test
    @DisplayName("노선에 역을 추가한다.")
    void addStation() {
        Line line = LineTestFixture.노선을_생성하고_하나의_역을_등록한다("1호선", "red", upStation, downStation, 10, 100);
        line.addStation(downStation, new Station("온수역"), 5);

        노선의_역이_파라미터와_동일한지_비교한다(line, "소사역", "부천역", "역곡역", "온수역");
    }

    @Test
    @DisplayName("추가하려는 구간의 거리가 1보다 작으면 실패한다.")
    void addMinusDistanceStation() {
        Line line = LineTestFixture.노선을_생성한다("1호선", "red", upStation, downStation, 10, 100);

        assertThatThrownBy(() -> line.addStation(upStation, new Station("부천역"), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선에 존재하는 구간을 추가하면 실패한다.")
    void addAlreadyExistStation() {
        Line line = LineTestFixture.노선을_생성하고_하나의_역을_등록한다("1호선", "red", upStation, downStation, 10, 100);

        assertThatThrownBy(() -> line.addStation(upStation, new Station("부천역"), 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("같은 길이의 구간을 등록하면 실패한다.")
    void addSameDistanceStation() {
        Line line = LineTestFixture.노선을_생성하고_하나의_역을_등록한다("1호선", "red", upStation, downStation, 10, 100);

        assertThatThrownBy(() -> line.addStation(upStation, new Station("온수역"), 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선에 존재하지 않는 구간을 추가하면 실패한다.")
    void addNonExistStaion() {
        Line line = LineTestFixture.노선을_생성한다("1호선", "red", upStation, downStation, 10, 100);
        assertThatThrownBy(() -> line.addStation(new Station("부평역"), new Station("부천역"), 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선에서 역을 삭제한다.")
    void removeStation() {
        Line line = LineTestFixture.노선을_생성하고_하나의_역을_등록한다("1호선", "red", upStation, downStation, 10, 100);
        line.removeLineStation(upStation);

        노선의_역이_파라미터와_동일한지_비교한다(line, "부천역", "역곡역");
    }

    @Test
    @DisplayName("마지막 구간을 삭제하면 실패한다.")
    void removeStationOfLastSection() {
        Line line = LineTestFixture.노선을_생성한다("1호선", "red", upStation, downStation, 10, 100);
        assertThatThrownBy(() -> line.removeLineStation(upStation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 노선의_역이_파라미터와_동일한지_비교한다(Line line, String... stationName) {
        assertThat(line.getStations()
                .stream()
                .map(s -> s.getName())
                .collect(Collectors.toList()))
                .containsExactly(stationName);
    }
}
