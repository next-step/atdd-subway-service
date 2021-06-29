package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("운임 테스트")
public class FareTest {

    @DisplayName("무료 확인")
    @Test
    void isFree() {
        assertThat(Fare.wonOf(0).isFree()).isTrue();
    }

    @DisplayName("더하기")
    @Test
    void plus() {
        assertThat(Fare.wonOf(1).plus(2)).isEqualTo(Fare.wonOf(3));
    }

    @DisplayName("빼기")
    @Test
    void minus() {
        assertThat(Fare.wonOf(3).minus(2)).isEqualTo(Fare.wonOf(1));
    }

    @DisplayName("할인율 적용")
    @Test
    void applyDiscountRate() {
        assertThat(Fare.wonOf(100).applyDiscountRate(50)).isEqualTo(Fare.wonOf(50));
    }
}
