package nextstep.subway.wrapped;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MoneyTest {
    @Test
    @DisplayName("돈은 음수일 경우 IllegalArgumentException이 발생한다")
    void 돈은_음수일_경우_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Money(-1));
    }

    @Test
    @DisplayName("돈은 더할 수 있다")
    void 돈은_더할_수_있다() {
        assertThat(new Money(1000).plus(new Money(1000)))
                .isEqualTo(new Money(2000));
    }

    @Test
    @DisplayName("돈은 뺄 수 있다")
    void 돈은_뺄_수_있다() {
        assertThat(new Money(3000).minus(new Money(1000)))
                .isEqualTo(new Money(2000));
    }

    @Test
    @DisplayName("돈은 곱할 수 있다")
    void 돈은_곱할_수_있다() {
        assertThat(new Money(3).multi(new Money(1000)))
                .isEqualTo(new Money(3000));
    }

    @Test
    @DisplayName("돈은 나눌 수 있다")
    void 돈은_나눌_수_있다() {
        assertThat(new Money(3000).divide(new Money(3)))
                .isEqualTo(new Money(1000));
    }

}