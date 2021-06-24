package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    @Test
    void create() {
        Line line1 = new Line("1호선", "blue");
        Line line2 = new Line("2호선", "green");
        List<Line> lineItems = Arrays.asList(line1, line2);

        Lines lines = new Lines(lineItems);

        assertThat(lines).isNotNull();
    }
}
