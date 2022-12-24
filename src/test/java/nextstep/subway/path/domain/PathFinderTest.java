package nextstep.subway.path.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    private Station 양재역;
    private Station 교대역;
    private Station 강남역;
    private Station 마곡역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Section 추가구간;
    private List<Line> 전체노선;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    
    @BeforeEach
    void setUp() {
        양재역 = new Station(1L, "양재역");
        교대역 = new Station(2L, "교대역");
        강남역 = new Station(3L ,"강남역");
        마곡역 = new Station(4L, "마곡역");
        남부터미널역 = new Station(5L, "남부터미널역");

        신분당선 = new Line("신분당선", "bg-orange-600", 강남역, 양재역, 10, Fare.from(900));
        이호선 = new Line("2호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "bg-orange-600", 교대역, 남부터미널역, 3, Fare.from(500));

        추가구간 = new Section(삼호선, 남부터미널역, 양재역, 2);
        삼호선.addSection(추가구간);

        전체노선 = Arrays.asList(삼호선, 이호선, 신분당선);
    }

    @Test
    @DisplayName("2가지 이상의 경로가 있는 경우 최적경로 조회")
    void findShortestPath() {
        Path path = PathFinder.of(전체노선).findShortestPath(교대역, 양재역);

        assertThat(path.getStationIds()).containsSequence(교대역.getId(), 남부터미널역.getId(), 양재역.getId());
    }

    @DisplayName("동일역으로 최단 경로를 조회하면 예외가 발생한다")
    @Test
    void sameStationFindPath() {
        assertThatThrownBy(
                () -> PathFinder.of(전체노선).findShortestPath(강남역, 강남역)
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결되지 않은 역으로 최단 경로를 조회하면 예외가 발생한다")
    @Test
    void nonStationFindPath() {
        assertThatThrownBy(
                () -> PathFinder.of(전체노선).findShortestPath(강남역, 마곡역)
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("경로의 요금을 계산한다.")
    @Test
    void 경로의_요금을_계산한다() {
        Path path = PathFinder.of(전체노선).findShortestPath(교대역, 양재역);
        FareCalculator fareCalculator = new FareCalculator(path);
        Fare fare = fareCalculator.calculate(null);

        // 1250 + 500 (거리 추가 요금)
        assertThat(fare.value()).isEqualTo(1750);
    }

    @DisplayName("어린이 요금을 계산한다.")
    @Test
    void 어린이_요금을_계산한다() {
        Path path = PathFinder.of(전체노선).findShortestPath(교대역, 양재역);
        FareCalculator fareCalculator = new FareCalculator(path);
        Fare fare = fareCalculator.calculate(12);

        // 1250 - ((1250 - 350) * 0.5) + 500 (거리 추가 요금)
        assertThat(fare.value()).isEqualTo(1300);
    }

}
