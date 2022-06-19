package study.unit;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
public class UnitTest {
    @Test
    void update() {
        // given
        String newName = "구분당선";

        Line line = new Line("신분당선", "RED", 0);
        Line newLine = new Line(newName, "GREEN", 10);

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(newName);
        assertThat(line.getExtraFare()).isEqualTo(10);
    }
}
