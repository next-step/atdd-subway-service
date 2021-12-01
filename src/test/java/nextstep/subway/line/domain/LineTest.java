package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    public static final Line 신분당선 = new Line("신분당선", "bg-red-600");
    private Line LINE_2;

    @BeforeEach
    void beforeEach() {
        LINE_2 = new Line("2호선", "bg-green-500");
    }

    @DisplayName("노선 생성")
    @Test
    void createLine() {
        Line actual = new Line("신분당선", "bg-red-600");
        assertThat(actual).isEqualTo(신분당선);
    }

    @DisplayName("구간을 포함한 노선 생성")
    @Test
    void createLineAndSection() {
        Line actual = new Line("2호선", "bg-green-500");
        Section section = new Section(actual, 양재역, 역삼역, 10);
        actual.addSection(section);

        LINE_2.addSection(section);

        assertThat(actual.getSections()).containsExactly(section);
        assertThat(actual).isEqualTo(LINE_2);
    }

    @DisplayName("노선 수정")
    @Test
    void updateLine() {
        Line updatedLine = new Line("1호선", "bg-blue-600");

        LINE_2.update(updatedLine);

        assertThat(LINE_2).isEqualTo(updatedLine);
    }
}
