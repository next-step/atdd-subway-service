package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @DisplayName("추가 요금이 Default로 0원으로 되는지 확인")
    @Test
    void createLine() {
        Line line = new Line();
        assertThat(line.getAddFare()).isEqualTo(0);
    }
}
