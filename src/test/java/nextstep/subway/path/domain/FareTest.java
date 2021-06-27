package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("운임 테스트")
public class FareTest {

    @DisplayName("무료 확인")
    @Test
    void isFree() {
        assertThat(new Fare(0).isFree()).isTrue();
    }

    @DisplayName("더하기")
    @Test
    void plus() {
        assertThat(new Fare(1).plus(2)).isEqualTo(new Fare(3));
    }

    @DisplayName("빼기")
    @Test
    void minus() {
        assertThat(new Fare(3).minus(2)).isEqualTo(new Fare(1));
    }

    @DisplayName("할인율 적용")
    @Test
    void applyDiscountRate() {
        assertThat(new Fare(100).applyDiscountRate(50)).isEqualTo(new Fare(50));
    }
}
