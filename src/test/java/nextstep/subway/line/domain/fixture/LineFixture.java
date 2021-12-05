package nextstep.subway.line.domain.fixture;

import nextstep.subway.line.domain.Line;

public class LineFixture {
    public static Line 일호선() {
        return new Line(1L, "ONE_LINE", "BLUE");
    }
    public static Line 이호선() {
        return new Line(2L, "TWO_LINE", "GREEN");
    }
    public static Line 삼호선() {
        return new Line(2L, "THREE_LINE", "ORANGE");
    }
}
