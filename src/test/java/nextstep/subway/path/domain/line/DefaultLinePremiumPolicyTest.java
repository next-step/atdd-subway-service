package nextstep.subway.path.domain.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.EfficientLines;
import nextstep.subway.path.domain.LinePremiumPolicy;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLinePremiumPolicyTest {

    @Test
    @DisplayName("운임을 계산한다")
    void 운임을_계산한다() {
        // given
        LinePremiumPolicy linePremiumPolicy = new DefaultLinePremiumPolicy();
        EfficientLines lines = new EfficientLines(LinePolicyFixture.환승_최대요금_7000);
        Money defaultMoney = new Money(5000);

        // when
        Money money = linePremiumPolicy.calcFare(lines, defaultMoney);

        // then
        assertThat(money).isEqualTo(new Money(12000));
    }
}