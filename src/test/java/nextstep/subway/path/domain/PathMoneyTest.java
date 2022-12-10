package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 추가 요금 테스트")
class PathMoneyTest {

    @DisplayName("곱하기")
    @Test
    void multiply_money_success() {
        // given:
        final double source = 100;
        final int target = 2;
        final double expected = 200;
        final PathMoney sourceMoney = PathMoney.from(Money.from(source));
        // when,then:
        assertThat(sourceMoney.mul(target)).isEqualTo(PathMoney.from(Money.from(expected)));
    }
}
