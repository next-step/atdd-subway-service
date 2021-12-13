package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathServiceTest {

    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 마곡나루역;
    private Station 가양역;
    private Station 부산역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 신논현역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        마곡나루역 = new Station("마곡나루역");
        가양역 = new Station("가양역");
        부산역 = new Station("부산역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addLineSection(교대역, 남부터미널역, 3);
        신논현역 = new Line("신논현역", "bg-red-600", 마곡나루역, 가양역, 5);
    }

    @DisplayName("최단 경로와 최단 거리 찾기")
    @Test
    void findPath() {
        //when
        PathService pathService = new PathService();
        SubwayPath path = pathService.findPath(Arrays.asList(신분당선, 이호선, 삼호선, 신논현역), 교대역, 양재역);

        //then
        assertThat(path.getStations()).isEqualTo(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @DisplayName("출발역과 도착역이 같을 경우 예외 처리")
    @Test
    void findPath_Same_Start_End_Station() {
        //when
        PathService pathService = new PathService();
        assertThatThrownBy(() -> {
            pathService.findPath(Arrays.asList(신분당선, 이호선, 삼호선, 신논현역), 교대역, 교대역);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("도착역과 출발역은 같을 수 없습니다.");;
    }

    @DisplayName("출발역과 도착역이 연결이 되어있지 않은 경우 예외 처리")
    @Test
    void findPath_Station_Not_Connect() {
        //when
        PathService pathService = new PathService();
        assertThatThrownBy(() -> {
            pathService.findPath(Arrays.asList(신분당선, 이호선, 삼호선, 신논현역), 교대역, 가양역);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우 예외 처리")
    @Test
    void findPath_Station_Not_Exist() {
        //when
        PathService pathService = new PathService();
        assertThatThrownBy(() -> {
            pathService.findPath(Arrays.asList(신분당선, 이호선, 삼호선, 신논현역), 교대역, 부산역);
                }).isInstanceOf(IllegalArgumentException.class);
    }
}