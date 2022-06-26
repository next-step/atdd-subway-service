package nextstep.subway.line.domain;

import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LinesTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Lines lines;


    /**
     * 교대역    --- *2호선* (7) ---   강남역
     * |                              |
     * *3호선*                      *신분당선*
     * (3)                           (10)
     * |                              |
     * 남부터미널역  --- *3호선* (2)---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 7);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        삼호선.addSection(교대역, 남부터미널역, 3);

        lines = new Lines(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("강남역과 교대역의 최단 거리 조회")
    @Test
    void findPathTest() {

        //when
        Path path = lines.findPath(강남역, 교대역);

        //then
        assertThat(path.getDistance()).isEqualTo(7);
    }


    @Test
    void hasStationTest() {
        //given
        Station 구로역 = new Station("구로역");

        //when
        boolean containResult = lines.hasStation(강남역);
        boolean notContainResult = lines.hasStation(구로역);

        //then
        assertTrue(containResult);
        assertFalse(notContainResult);
    }

    @Test
    void 출발지와_도착지가_같은_경우_최단_거리_조회시_에러_발생() {
        assertThrows(IllegalArgumentException.class, () -> lines.findPath(강남역, 강남역));
    }

    @Test
    void 등록되지_않은_역을_최단_거리_조회시_에러_발생() {
        //given
        Station 구로역 = new Station("구로역");

        assertThrows(NoSuchElementFoundException.class, () -> lines.findPath(강남역, 구로역));
    }
}
