package nextstep.subway.path.domain.graph;

import nextstep.subway.exception.CannotFindPathException;
import nextstep.subway.exception.CannotGenerateStationGraphException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathExceptionCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("그래프 객체 테스트")
class StationGraphTest {

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "bg-green-600", 0);
        신분당선 = new Line("신분당선", "bg-red-600", 0);
        삼호선 = new Line("3호선", "bg-orange-600", 0);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
    }

    @Test
    void 노선에_구간이_하나도_없는_경우_CannotGenerateStationGraphException_발생() {
        assertThatThrownBy(() -> {
            new StationGraph(Arrays.asList());
        }).isInstanceOf(CannotGenerateStationGraphException.class)
                .hasMessage(PathExceptionCode.CANNOT_GENERATE_STATION_GRAPH.getMessage());
    }

    @Test
    void 입력된_지하철역이_그래프에_포함되었는지_확인한다() {
        StationGraph stationGraph = new StationGraph(Arrays.asList(new Section(이호선, 교대역, 강남역, 10)));

        assertAll(
                () -> assertTrue(stationGraph.containsStation(교대역)),
                () -> assertTrue(stationGraph.containsStation(강남역))
        );
    }

    @Test
    void 출발역과_도착역이_연결되어_있지_않으면_CannotFindPathException_발생() {
        Section 교대역_강남역 = new Section(이호선, 교대역, 강남역, 10);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 5);

        StationGraph stationGraph = new StationGraph(Arrays.asList(
                교대역_강남역, 남부터미널역_양재역));

        assertThatThrownBy(() -> {
            stationGraph.findShortestPath(교대역, 양재역);
        }).isInstanceOf(CannotFindPathException.class)
                .hasMessage(PathExceptionCode.NOT_CONNECTED_SOURCE_AND_TARGET.getMessage());
    }

    @Test
    void 출발역과_도착역까지의_구간_중_최단_거리를_찾음() {
        Section 강남역_양재역 = new Section(신분당선, 강남역, 양재역, 10);
        Section 교대역_강남역 = new Section(이호선, 교대역, 강남역, 10);
        Section 교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 3);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 2);

        StationGraph stationGraph = new StationGraph(Arrays.asList(
           강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));

        assertThat(stationGraph.findShortestPath(양재역, 교대역)).satisfies(path -> {
            assertThat(path.getStations()).containsExactly(양재역, 남부터미널역, 교대역);
            assertEquals(5, path.getDistance());
        });
    }
}
