package nextstep.subway.line.domain.fixture;

import nextstep.subway.line.domain.Line;

public class LineFixture {
    public static Line 일호선() {
        return new Line(1L, "ONE_LINE", "BLUE");
    }
}
