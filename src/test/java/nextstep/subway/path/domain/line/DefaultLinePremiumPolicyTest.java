package nextstep.subway.path.domain.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultLinePremiumPolicyTest {

    @Test
    void calcFare() {
        Lines lines = new Lines(
                Arrays.asList(
                        new Line("1호선", "1호선", 1000),
                        new Line("2호선", "2호선", 2000),
                        new Line("3호선", "3호선", 3000),
                        new Line("4호선", "4호선", 500),
                        new Line("5호선", "5호선", 7000)
                )
        );

        assertThat(new DefaultLinePremiumPolicy().calcFare(lines, new Money(5000)))
                .isEqualTo(new Money(12000));
    }
}