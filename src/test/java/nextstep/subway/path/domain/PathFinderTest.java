package nextstep.subway.path.domain;

import static nextstep.subway.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.AdditionalFee;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private List<Section> 모든구간;
    private PathFinder pathFinder;

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

        신분당선 = new Line("신분당선", "red", new AdditionalFee(100));
        이호선 = new Line("이호선", "green", new AdditionalFee(0));
        삼호선 = new Line("삼호선", "orange", new AdditionalFee(500));

        모든구간 = new ArrayList<>(Arrays.asList(new Section(신분당선, 강남역, 양재역, new Distance(10)),
            new Section(이호선, 교대역, 강남역, new Distance(10)),
            new Section(삼호선, 교대역, 남부터미널역, new Distance(3)),
            new Section(삼호선, 남부터미널역, 양재역, new Distance(2))
        ));

        pathFinder = new PathFinder(new StationGraph(모든구간));
    }

    @DisplayName("경로 조회의 결과 역아이디 리스트와 거리를 반환, 노선추가요금 있을 때 최대요금만 반영 - 환승했을 때 더 빠름")
    @Test
    void find1() {
        // when
        Path result = pathFinder.find(강남역, 남부터미널역);

        // then
        assertThat(result.getDistance()).isEqualTo(new Distance(12));
        assertThat(result.getStations()).containsExactlyElementsOf(
            Arrays.asList(강남역, 양재역, 남부터미널역));
        assertThat(result.getFare()).isEqualTo(new Fare(1250 + 100 + 500));
    }

    @DisplayName("경로 조회의 결과 역아이디 리스트와 거리를 반환 - 환승 안했을 때 더 빠름")
    @Test
    void find2() {
        // when
        Path result = pathFinder.find(교대역, 양재역);

        // then
        assertThat(result.getDistance()).isEqualTo(new Distance(5));
        assertThat(result.getStations()).containsExactlyElementsOf(
            Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(result.getFare()).isEqualTo(new Fare(1250 + 500));
    }

    @DisplayName("출발지와 도착지가 같은 경우 예외처리한다")
    @Test
    void find3() {
        // when/then
        assertThatThrownBy(() -> pathFinder.find(교대역, 교대역))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(PATH_SAME_STATION);
    }

    @DisplayName("출발역과 도착역은 전체 구간에 포함되어있어야 한다")
    @Test
    void find4() {
        // when/then
        assertThatThrownBy(() -> pathFinder.find(교대역, new Station(99L, "아무역")))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(PATH_MUST_CONTAIN_GRAPH);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
    @Test
    void find5() {
        // given
        Station 혜화역 = new Station(5L, "혜화역");
        Station 이수역 = new Station(6L, "숙대입구역");
        Line 사호선 = new Line("사호선", "blue");
        모든구간.add(new Section(사호선, 이수역, 혜화역, new Distance(10)));
        pathFinder = new PathFinder(new StationGraph(모든구간));

        // when/then
        assertThatThrownBy(() -> pathFinder.find(교대역, 혜화역))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(PATH_NOT_CONNECTED);
    }

    @DisplayName("환승 안하고 노선에 추가요금 없을 때")
    @Test
    void find6() {
        // when
        Path result = pathFinder.find(교대역, 강남역);

        // then
        assertThat(result.getDistance()).isEqualTo(new Distance(10));
        assertThat(result.getStations()).containsExactlyElementsOf(
            Arrays.asList(교대역, 강남역));
        assertThat(result.getFare()).isEqualTo(new Fare(1250));
    }
}
