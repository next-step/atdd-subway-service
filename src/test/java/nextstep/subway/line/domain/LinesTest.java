package nextstep.subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class LinesTest {

    @DisplayName("지하철 노선목록으로 부터 가장 큰 노선요금을 찾는다.")
    @Test
    void findMaxLineFare() {
        Line 신분당선 = new Line("신분당선", "red", 100);
        Line 분당선 = new Line("분당선", "yellow", 200);
        Line 일호선 = new Line("일호선", "blue", 50);

        Lines lines = Lines.of(Arrays.asList(신분당선, 분당선, 일호선));
        LineFare fare = lines.findMaxLineFare();

        Assertions.assertThat(fare).isEqualTo(LineFare.from(200));
    }
}
