package nextstep.subway.path;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineFixtures {
    public static Station 강남역 = Station.of(1L, "강남역");
    public static Station 양재역 = Station.of(2L, "양재역");
    public static Station 교대역 = Station.of(3L, "교대역");
    public static Station 남부터미널역 = Station.of(4L, "남부터미널역");

    public static List<Line> 모든_노선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    static {
        Line 신분당선 = Line.of("신분당선", "노랑");
        Line 이호선 = Line.of("신분당선", "초록");
        Line 삼호선 = Line.of("신분당선", "주황");

        신분당선.addLineStation(Section.of(신분당선, 강남역, 양재역, 10));
        이호선.addLineStation(Section.of(이호선, 교대역, 강남역, 10));
        삼호선.addLineStation(Section.of(삼호선, 교대역, 양재역, 5));
        삼호선.addLineStation(Section.of(삼호선, 교대역, 남부터미널역, 3));

        모든_노선 = Arrays.asList(신분당선, 이호선, 삼호선);
    }
}
