package nextstep.subway.line.domain.fixture;

import nextstep.subway.line.domain.Line;

public class LineFixture {
    public static Line 일호선() {
        return new Line(1L, "ONE_LINE", "BLUE", 1000L);
    }
    public static Line 이호선() {
        return new Line(2L, "TWO_LINE", "GREEN", 2000L);
    }
    public static Line 삼호선() {return new Line(3L, "THREE_LINE", "ORANGE", 0L);}
    public static Line 사호선() { return new Line(4L, "FOUR_LINE", "sky",500L);}
}
