package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("라인 이름과 color 정보를 업데이트한다")
    void update() {
        Line line = new Line("력삼역", "color-1");
        line.update(new Line("력삼-센터필드역", "color-4"));

        assertThat(line.getName()).isEqualTo("력삼-센터필드역");
        assertThat(line.getColor()).isEqualTo("color-4");
    }
}
