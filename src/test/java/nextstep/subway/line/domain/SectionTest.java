package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.일호선;
import static nextstep.subway.line.domain.StationTest.강남역;
import static nextstep.subway.line.domain.StationTest.서울역;
import static nextstep.subway.line.domain.StationTest.신용산역;
import static nextstep.subway.line.domain.StationTest.신촌역;
import static nextstep.subway.line.domain.StationTest.양평역;
import static nextstep.subway.line.domain.StationTest.역삼역;
import static nextstep.subway.line.domain.StationTest.용문역;
import static nextstep.subway.line.domain.StationTest.용산역;

public class SectionTest {

    public static final Section 일호선_구간_강남역_신촌역 = new Section(1L, 일호선, 강남역, 신촌역, 10);
    public static final Section 일호선_구간_신촌역_역삼역 = new Section(2L, 일호선, 신촌역, 역삼역, 5);
    public static final Section 일호선_구간_용산역_역삼역 = new Section(3L, 일호선, 용산역, 역삼역, 4);
    public static final Section 일호선_구간_역삼역_서울역 = new Section(4L, 일호선, 역삼역, 서울역, 4);
    public static final Section 일호선_추가안한_서울역_신용산역 = new Section(5L, 일호선, 서울역, 신용산역, 2);
    public static final Section 일호선_존재하지않는_용문역_양평역 = new Section(6L, 일호선, 용문역, 양평역, 2);

}
