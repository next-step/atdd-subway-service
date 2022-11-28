package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.Test;

public class LineTest {
    private final String 수인분당선 = "수인분당선";
    private final String 신분당선 = "신분당선";
    private final String YELLOW = "yellow";
    private final String RED = "red";

    @Test
    void 생성() {
        Line line = Line.of(신분당선, RED);
        assertAll(
                () -> assertThat(line.getName()).isEqualTo(신분당선),
                () -> assertThat(line.getColor()).isEqualTo(RED)
        );
    }

    @Test
    void 노선_수정() {
        Line line = Line.of(신분당선, RED);
        line.update(수인분당선, YELLOW);
        assertAll(
                () -> assertThat(line.getName()).isEqualTo(수인분당선),
                () -> assertThat(line.getColor()).isEqualTo(YELLOW)
        );
    }
}
