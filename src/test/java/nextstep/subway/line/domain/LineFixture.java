package nextstep.subway.line.domain;

import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.station.StationFixture.*;

public class LineFixture {

    public static Line 이호선() {
        return new Line("2호선", "Yellow-green", 당산역(), 합정역(), new Distance(20));
    }
    public static Line 신이호선() {
        return new Line("신2호선", "Black", 당산역(), 합정역(), new Distance(20));
    }

    public static Line 일호선() {
        Line 일호선 = new Line("1호선", "Blue", 서울역(), 시청역(), new Distance(30));
        ReflectionTestUtils.setField(일호선, "id", 1L);
        return 일호선;
    }

}