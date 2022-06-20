package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("노선이 변경 된다.")
    public void update() {
        Line line = new Line("신분당선", "bg-red-500");

        line.update(new Line("1호선", "bg-blue-500"));

        assertAll(
                () -> assertThat(line.getColor()).isEqualTo("bg-blue-500"),
                () -> assertThat(line.getName()).isEqualTo("1호선")
        );
    }
}
