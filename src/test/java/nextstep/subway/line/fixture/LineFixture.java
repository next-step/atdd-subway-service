package nextstep.subway.line.fixture;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;

import static nextstep.subway.station.fixture.StationFixture.*;

public class LineFixture {
    public static Line 오호선 = new Line(1L, "오호선", "bg-red-600", 양평역, 영등포구청역, new Distance(10));
    public static Line 이호선 = new Line(2L, "이호선", "bg-red-600", 영등포구청역, 당산역, new Distance(10));
    public static Line 일호선 = new Line(3L, "일호선", "bg-red-600", 신길역, 영등포역, new Distance(5));
    public static Line 신분당선 = new Line(4L, "신분당선", "bg-red-600", 야탑역, 모란역, new Distance(5));
}
