package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.일호선_구간_강남역_신촌역;
import static nextstep.subway.line.domain.SectionTest.일호선_구간_신촌역_역삼역;
import static nextstep.subway.line.domain.StationTest.강남역;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LineTest {

    public static final Line 일호선 = new Line(1L, "ONE_LINE", "BLUE");
    public static final Line 이호선 = new Line(2L, "TWO_LINE", "GREEN");


}