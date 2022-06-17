package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class RouteTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역 , 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

        new Section(삼호선, 교대역, 남부터미널역, 3);
    }


    /*
    * Given 많은 지하철역이 저장되어 있고
    * When 해당 지하철 노선 역중 시작역과 도착역을 입력하면
    * Then 최단 경로를 알수 있다.
    * */
    @DisplayName("시작역과 도착역을 입력하면 최단 경로를 찾을수 있다.")
    @Test
    void getShortestRouteTest() {
        // when
        PathResponse pathResponse = PathResponse.of(new Route().getShortestRoute(Arrays.asList(신분당선, 이호선, 삼호선),교대역, 양재역));

        // then
        List<String> responseStationNames = pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList());
        assertThat(responseStationNames).containsExactlyElementsOf(Arrays.asList(양재역.getName(), 남부터미널역.getName(), 교대역.getName()));
    }
}