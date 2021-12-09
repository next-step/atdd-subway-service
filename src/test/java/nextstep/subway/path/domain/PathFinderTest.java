package nextstep.subway.path.domain;

import nextstep.subway.error.CommonException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

    private Station upStation;
    private Station downStation;
    private Line line;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        upStation = new Station("건대역");
        downStation = new Station("용마산역");
        line = new Line("7호선", "bg-red-600", upStation, downStation, 10);

        pathFinder = new PathFinder();
    }

    @DisplayName("최단거리 경로 검증")
    @Test
    void findPaths() {
        List<Line> lines = Arrays.asList(line);
        PathFinder pathFinder = new PathFinder();

        Path path = pathFinder.findPath(lines, upStation, downStation);

        assertThat(path.getStations().size()).isEqualTo(2);
        assertThat(path.getDistance()).isEqualTo(10);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void sameStation() {
        List<Line> lines = Arrays.asList(line);

        assertThatThrownBy(() -> {
            pathFinder.findPath(lines, upStation, upStation);
        }).isInstanceOf(CommonException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void notConnectStation() {
        Station notConnectUpStation = new Station("강남역");
        Station notConnectDonwStation = new Station("사당역");
        Line notConnectLine = new Line("2호선", "bg-red-600", notConnectUpStation, notConnectDonwStation, 10);
        List<Line> lines = Arrays.asList(line, notConnectLine);

        assertThatThrownBy(() -> {
            pathFinder.findPath(lines, upStation, notConnectUpStation);
        }).isInstanceOf(CommonException.class)
                .hasMessageContaining("출발역과 도착역이 이어져 있지 않습니다.");
    }
}
