package nextstep.subway.line.domain;

import static nextstep.subway.station.domain.StationTest.*;

public class SectionTest {
    public static final Line 신분당선 = new Line("신분당선", "빨간색");

    public static final Section 강남_양재_100 = new Section(1L, 신분당선, 강남역, 양재역, 100);
    public static final Section 양재_광교중앙_30 = new Section(2L, 신분당선, 양재역, 광교중앙역, 30);
    public static final Section 광교중앙_광교_30 = new Section(3L, 신분당선, 광교중앙역, 광교역, 30);
}
