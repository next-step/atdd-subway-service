package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("할인을 적용한 최종 요금 계산")
class FareTest {

    private static DiscountPolicy policy = DiscountPolicy.ADULT;

    @Test
    @DisplayName("Fare 클래스로 더하기 수행")
    void plus() {
        Fare fare = Fare.of(policy);

        assertThat(fare.plus(1000)).isEqualTo(Fare.of(2250, policy));
    }

    @Test
    @DisplayName("Fare class로 할인된 요금 계산, 어른은 할인 없음")
    void discount() {
        Fare fare = Fare.of(policy);
        assertThat(Fare.of(fare.discount(), policy)).isEqualTo(Fare.of(policy));
    }
}
