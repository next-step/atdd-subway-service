package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");

        신분당선 = new Line(1L, "신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addLineStation(교대역, 남부터미널역, 3);
    }

    @DisplayName("정상 경로 조회")
    @Test
    void getPath() {
        //given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        //when
        PathResponse pathResponse = pathFinder.getPath(교대역, 양재역);

        //then
        List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactlyElementsOf(
                Stream.of(교대역, 남부터미널역, 양재역)
                        .map(Station::getName)
                        .collect(Collectors.toList())
        );
        assertThat(pathResponse.getDistance()).isEqualTo(5);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void getPathSameSourceTarget() {
        //given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        //when
        //then
        assertThatThrownBy(() -> {
            PathResponse pathResponse = pathFinder.getPath(교대역, 교대역);
        }).isInstanceOf(RuntimeException.class);
    }


    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void getPathNoLink() {
        //given
        Station 도봉산역 = new Station("도봉산역");
        Station 망월사역 = new Station("망월사역");
        Line 일호선 = new Line("일호선", "bg-red-600", 망월사역, 도봉산역, 15);
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 일호선));

        //when
        //then
        assertThatThrownBy(() -> {
            pathFinder.getPath(교대역, 망월사역);
        }).isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> {
            pathFinder.getPath(망월사역, 교대역);
        }).isInstanceOf(RuntimeException.class);
    }


    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void getPathNoStation() {
        //given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        //when
        //then
        assertThatThrownBy(() -> {
            pathFinder.getPath(교대역, new Station("사당역"));
        }).isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> {
            pathFinder.getPath(new Station("사당역"), 교대역);
        }).isInstanceOf(RuntimeException.class);
    }
}