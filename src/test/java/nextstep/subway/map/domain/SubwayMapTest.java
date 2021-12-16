package nextstep.subway.map.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubwayMapTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private SubwayMap 지도;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = Line.of("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = Line.of("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        삼호선.addSection(교대역, 남부터미널역, 3);

        지도 = SubwayMap.of(Arrays.asList(
                Section.of(신분당선, 강남역, 양재역, 10)
                , Section.of(이호선, 교대역, 강남역, 10)
                , Section.of(삼호선, 교대역, 남부터미널역, 3)
                , Section.of(삼호선, 남부터미널역, 양재역, 2)));
    }

    @DisplayName("출발역과 도착역의 최단 경로 조회 - 직행")
    @Test
    void findShortestPath_straight() {
        // given
        Station source = 강남역;
        Station target = 교대역;

        // when
        PathResponse shortestPath = 지도.findShortestPath(source, target);

        // then
        assertAll(
                () -> assertThat(shortestPath.getStations()).containsExactly(toStationResponse(강남역, 교대역))
                , () -> assertThat(shortestPath.getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("출발역과 도착역의 최단 경로 조회 - 환승")
    @Test
    void findShortestPath_transfer() {
        // given
        Station source = 강남역;
        Station target = 남부터미널역;

        // when
        PathResponse shortestPath = 지도.findShortestPath(source, target);

        // then
        assertAll(
                () -> assertThat(shortestPath.getStations()).containsExactly(toStationResponse(강남역, 양재역, 남부터미널역))
                , () -> assertThat(shortestPath.getDistance()).isEqualTo(12)
        );
    }

    private StationResponse[] toStationResponse(Station... stations) {
        return Stream.of(stations).map(StationResponse::of).toArray(StationResponse[]::new);
    }
}
