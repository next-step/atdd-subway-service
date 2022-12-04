package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixture.이호선;
import static nextstep.subway.station.StationFixture.*;

public class SectionFixture {

    public static Section 영등포구청_당산_구간(Line line) {
        return new Section(line, 영등포구청역(), 당산역(), new Distance(10));
    }

    public static Section 당산_합정_구간(Line line) {
        return new Section(line, 당산역(), 합정역(), new Distance(7));
    }

    public static Section 합정_홍대입구_구간(Line line) {
        return new Section(line, 합정역(), 홍대입구역(), new Distance(10));
    }

    public static Section 당산_홍대입구_구간(Line line) {
        return new Section(line, 당산역(), 홍대입구역(), new Distance(20));
    }

    public static Section 여의_여의나루_구간(Line line) {
        return new Section(line, 여의도역(), 여의나루역(), new Distance(10));
    }

}
