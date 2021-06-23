package nextstep.subway.line.domain;

import static nextstep.subway.station.domain.StationTest.*;

public class SectionTest {
    public static final Line 신분당선 = new Line("신분당선", "빨간색");

    public static final Section 강남_광교_100 = new Section(1L, 신분당선, 강남역, 광교역, 100);
}
