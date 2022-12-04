package nextstep.subway.line.domain;

import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class LinesTest {

    @Test
    void 지하철_노선_목록에서_가장_큰_노선요금을_찾는다() {
        Line 신분당선 = new Line("신분당선", "red", 0);
        Line 분당선 = new Line("분당선", "yellow", 900);
        Line 일호선 = new Line("일호선", "blue", 500);

        Lines lines = Lines.of(Arrays.asList(신분당선, 분당선, 일호선));
        LineFare fare = lines.findMaxLineFare();

        Assertions.assertThat(fare).isEqualTo(LineFare.from(900));
    }
}
