package nextstep.subway.line.fixture;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

import static java.util.Arrays.asList;
import static nextstep.subway.station.domain.StationFixture.*;
import static nextstep.subway.line.fixture.LineFixture.*;

public class SectionFixture {
    public static Section 구간_양평역_영등포구청역 = new Section(1L, 오호선, 양평역, 영등포구청역, new Distance(10));
    public static Section 구간_영등포구청역_영등포시장역 = new Section(2L, 오호선, 영등포구청역, 영등포시장역, new Distance(5));
    public static Section 구간_영등포시장역_신길역 = new Section(3L, 오호선, 영등포시장역, 신길역, new Distance(10));
    public static Section 구간_신길역_여의도역 = new Section(4L, 오호선, 신길역, 여의도역, new Distance(10));
    public static Section 구간_영등포구청역_당산역 = new Section(5L, 이호선, 영등포구청역, 당산역, new Distance(10));
    public static Section 구간_신길역_영등포역 = new Section(6L, 오호선, 신길역, 영등포역, new Distance(5));
    public static Section 구간_야탑역_모란역 = new Section(7L, 신분당선, 야탑역, 모란역, new Distance(5));

    public static Sections 전체_구간 = new Sections(asList(구간_양평역_영등포구청역
            , 구간_영등포구청역_영등포시장역
            , 구간_영등포시장역_신길역
            , 구간_신길역_여의도역
            , 구간_영등포구청역_당산역
            , 구간_영등포구청역_영등포시장역
            , 구간_신길역_영등포역
            , 구간_야탑역_모란역));
}
