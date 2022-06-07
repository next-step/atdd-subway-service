package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("해당 객체가 같은지 검증")
    void verifySameLine() {
        final Line twoLine = new Line("2호선", "yellow");
        assertThat(twoLine).isEqualTo(new Line("2호선", "yellow"));
    }

    @Test
    @DisplayName("노선 업데이트 확인")
    void updateLine() {
        final Line sinBunDang = new Line("신분당선", "red");
        Line line = new Line("2호선", "yellow");
        line.update(sinBunDang);

        assertThat(line).isEqualTo(sinBunDang);
    }
}
