package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFixtures {

    public static Station 교대 = new Station(1L, "교대");
    public static Station 강남 = new Station(2L, "강남");
    public static Station 양재 = new Station(3L, "양재");
    public static Station 남부터미널 = new Station(4L, "남부터미널");
    public static Station 천호 = new Station(5L, "천호");
    public static Station 강동구청 = new Station(6L, "강동구청");

    public static List<Line> 전체구간() {
        Line 이호선 = new Line("이호선", "red");
        Line 삼호선 = new Line("삼호선", "red");
        Line 신분당선 = new Line("신분당선", "red");
        Line 팔호선 = new Line("팔호선", "red");

        신분당선.addSection(강남, 양재, 10);
        이호선.addSection(교대, 강남, 10);
        삼호선.addSection(교대, 남부터미널, 2);
        삼호선.addSection(남부터미널, 양재, 3);
        팔호선.addSection(강동구청, 천호, 5);

        return Arrays.asList(이호선, 삼호선, 신분당선, 팔호선);
    }
}
