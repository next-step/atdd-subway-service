package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;

@DisplayName("Line 클래스")
public class LineTest {
    public static Line 섹션_없는_라인_생성(final String name, final String color) {
        return new Line(name, color);
    }
}
