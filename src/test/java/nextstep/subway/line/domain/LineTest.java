package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    private Line 신분당선;

    @BeforeEach
    void beforeEach() {
        신분당선 = new Line("신분당선", "bg-red-600");
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
        Line actual = new Line("신분당선", "bg-red-600");
        Section section = new Section(actual, 양재역, 역삼역, 10);
        actual.addSection(section);

        신분당선.addSection(section);

        assertThat(actual.getSections()).containsExactly(section);
        assertThat(actual).isEqualTo(신분당선);
    }

    @DisplayName("노선 수정")
    @Test
    void updateLine() {
        Line updatedLine = new Line("2호선", "bg-green-500");

        신분당선.update(updatedLine);

        assertThat(신분당선).isEqualTo(updatedLine);
    }
}
