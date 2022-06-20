package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * - VO class 이며 add, minus, discount 메소드를 가진다.
 * - add 메소드는 입력 받은 금액을 기존 금액과 합산하여 금액을 반환한다.
 * - minus 메소드는 입력 받은 금액만큼 기존 금액에서 제외하고 금액을 반환한다.
 * - discount 메소드는 discount 정책을 입력 받아서 discount 정책 결과 금액 만큼 기존 금액에서 할인해서 금액을 반환한다.
 */

class PriceTest {

    private Price defaultPrice;

    @BeforeEach
    void setUp() {
        defaultPrice = new Price(1250);
    }

    @DisplayName("동등성 비교 테스트")
    @Test
    void createTest() {
        assertThat(defaultPrice).isEqualTo(new Price(1250));
    }

    @DisplayName("Price 는 음수를 가질수 없다.")
    @Test
    void invalidCreateTest() {
        assertThatThrownBy(() -> new Price(-1))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력받은 금액만큼 요금이 증가된다.")
    @Test
    void addTest() {
        assertThat(defaultPrice.plus(new Price(900))).isEqualTo(new Price(2150));
    }

    @DisplayName("입력받은 금액만큼 요금이 감소된다.")
    @Test
    void minusTest() {
        assertThat(defaultPrice.minus(new Price(200))).isEqualTo(new Price(1050));
    }

}