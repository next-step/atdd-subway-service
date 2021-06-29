package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.*;

public class LineTest {
    public static final Line 신분당선 = new Line(1L, "신분당선", "레드", 300);
    public static final Line 이호선 = new Line(2L, "이호선", "그린");

    static {
        신분당선.addSection(강남_양재_100);
        신분당선.addSection(양재_광교중앙_30);
        신분당선.addSection(광교중앙_광교_30);

        이호선.addSection(강남_교대_30);
    }
}
