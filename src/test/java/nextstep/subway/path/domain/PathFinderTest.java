package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathFinderResult;
import nextstep.subway.station.domain.Station;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

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

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("이호선", "green");
        삼호선 = new Line("삼호선", "orange");

        신분당선.addSection(강남역, 양재역, 10);
        이호선.addSection(교대역, 강남역, 10);
        삼호선.addSection(교대역, 남부터미널역, 3);
        삼호선.addSection(남부터미널역, 양재역, 2);
    }

    @DisplayName("경로 조회의 결과 역아이디 리스트와 거리를 반환 - 환승했을 때 더 빠름")
    @Test
    void find1() {
        // given
        PathFinder pathFinder = new PathFinder();

        // when
        PathFinderResult result = pathFinder.find(Arrays.asList(신분당선, 이호선, 삼호선), 강남역.getId(), 남부터미널역.getId());

        // then
        assertThat(result.getDistance()).isEqualTo(12);
        assertThat(result.getStationsIds()).containsExactlyElementsOf(
            Arrays.asList(강남역.getId(), 양재역.getId(), 남부터미널역.getId()));
    }

    @DisplayName("경로 조회의 결과 역아이디 리스트와 거리를 반환 - 환승 안했을 때 더 빠름")
    @Test
    void find2() {
        // given
        PathFinder pathFinder = new PathFinder();

        // when
        PathFinderResult result = pathFinder.find(Arrays.asList(신분당선, 이호선, 삼호선), 교대역.getId(), 양재역.getId());

        // then
        assertThat(result.getDistance()).isEqualTo(5);
        assertThat(result.getStationsIds()).containsExactlyElementsOf(
            Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId()));
    }
}
