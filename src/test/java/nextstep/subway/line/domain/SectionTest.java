package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.일호선;
import static nextstep.subway.line.domain.StationTest.강남역;
import static nextstep.subway.line.domain.StationTest.서울역;
import static nextstep.subway.line.domain.StationTest.신촌역;
import static nextstep.subway.line.domain.StationTest.역삼역;
import static nextstep.subway.line.domain.StationTest.용산역;

public class SectionTest {

    public static final Section 일호선_구간_강남역_신촌역 = new Section(1L, 일호선, 강남역, 신촌역, 10);
    public static final Section 일호선_구간_신촌역_역삼역 = new Section(2L, 일호선, 신촌역, 역삼역, 5);
    public static final Section 일호선_구간_용산역_역삼역 = new Section(3L, 일호선, 용산역, 역삼역, 4);
    public static final Section 일호선_구간_역삼역_서울역 = new Section(4L, 일호선, 역삼역, 서울역, 4);

}
