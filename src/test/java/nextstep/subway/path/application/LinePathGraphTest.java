package nextstep.subway.path.application;

import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.message.LinePathMessage;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LinePathGraphTest {

    private LinePathGraph 이호선_신분당선_경로_그래프;

    @BeforeEach
    void setUp() {
        List<Line> lines = Arrays.asList(LineFixture.이호선, LineFixture.신분당선);
        이호선_신분당선_경로_그래프 = new LinePathGraph(lines);
    }


    /**
     * Given 시작역과 도착역이 주어진경우
     * When 최단 경로 조회시
     * Then 최단 경로를 반환한다
     */
    @DisplayName("신논현역 - 논현역의 최단 거리를 반환한다")
    @Test
    void get_shortest_path_test() {
        // when
        LinePath linePath = 이호선_신분당선_경로_그래프.getShortestPath(StationFixture.신논현역, StationFixture.교대역);

        // then
        assertAll(
                () -> assertThat(linePath.getDistance()).isEqualTo(new Distance(10)),
                () -> assertThat(getStationNames(linePath)).containsExactly("신논현역", "삼성역", "교대역")
        );
    }
    
    /**
     * When 지하철 경로 그래프에 중복 된 Edge 추가시
     * Then 예외처리 한다
     */
    @DisplayName("지하철 경로 그래프에 중복 된 Edge 추가시 예외 처리")
    @Test
    void add_station_to_graph_vertex_test() {
        // when & then
        assertThatThrownBy(() -> 이호선_신분당선_경로_그래프.addEdge(StationFixture.교대역, StationFixture.삼성역, new Distance(5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(LinePathMessage.GRAPH_ERROR_IS_ALREADY_ENROLLED_EDGE.message());
    }

    private List<String> getStationNames(LinePath linePath) {
        return linePath.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }
}
