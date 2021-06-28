package nextstep.subway.path.domain.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.EfficientLines;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLinePremiumPolicyTest {

    @Test
    void calcFare() {
        EfficientLines lines = new EfficientLines(LinePolicyFixture.환승_최대요금_7000);

        assertThat(new DefaultLinePremiumPolicy().calcFare(lines, new Money(5000)))
                .isEqualTo(new Money(12000));
    }
}