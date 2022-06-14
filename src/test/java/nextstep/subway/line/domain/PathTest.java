package nextstep.subway.line.domain;

import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선 10* ---   강남역
     * |                        |
     * *3호선 3*                   *신분당선 10*
     * |                        |
     * 남부터미널역  --- *3호선 2* ---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");


        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 남부터미널역, 3);
        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 2));
    }

    @Test
    void 최단구간_조회_교대_양재_구간() {
        PathResponse shortest = Path.of().findShortest(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역);
        assertThat(shortest.getDistance()).isEqualTo(5);
    }

}
