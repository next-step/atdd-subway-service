package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;

public class PathTestFixture {

    public Station 강남역;
    public Station 양재역;
    public Station 교대역;
    public Station 남부터미널역;
    public Line 신분당선;
    public Line _2호선;
    public Line _3호선;
    public List<Line> 노선목록;

    public Section 구간;

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

        신분당선 = new Line("신분당선", "red", 900, 강남역, 양재역, 10);
        _2호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        _3호선 = new Line("3호선", "orange", 500, 교대역, 양재역, 5);
        노선목록 = Arrays.asList(신분당선, _2호선, _3호선);

        구간 = new Section(_3호선, 교대역, 남부터미널역, 3);
        _3호선.addSection(구간);
    }
}
