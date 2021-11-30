package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.domain.StationTest.양재역;
import static nextstep.subway.station.domain.StationTest.역삼역;
import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    public static final Line LINE_신분당선 = new Line("신분당선", "bg-red-600");;

    @DisplayName("노선 생성")
    @Test
    void createLine() {
        Line expected = new Line("신분당선", "bg-red-600");
        assertThat(LINE_신분당선).isEqualTo(expected);
    }

    @DisplayName("구간을 포함한 노선 생성")
    @Test
    void createLineAndSection() {
        Line actual = new Line("신분당선", "bg-red-600", 역삼역, 양재역, 10);
    }
}
