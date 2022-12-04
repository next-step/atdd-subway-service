package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixture.*;

public class LineFixture {

    public static Line 이호선() {
        return new Line("2호선", "Yellow-green", 당산역(), 합정역(), new Distance(20));
    }
    public static Line 신이호선() {
        return new Line("신2호선", "Black", 당산역(), 합정역(), new Distance(20));
    }

}