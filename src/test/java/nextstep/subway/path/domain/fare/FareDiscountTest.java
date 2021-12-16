package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.path.domain.fare.policy.AdultDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.AgeDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.ChildDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.InfantDiscountPolicy;
import nextstep.subway.path.domain.fare.policy.YouthDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareDiscountTest {

    @DisplayName("영유아 요금 할인 테스트")
    @Test
    void testInfant() {
        AgeDiscountPolicy ageDiscountPolicy = new InfantDiscountPolicy();
        Fare lastFare = ageDiscountPolicy.calculateFare(Fare.BASE_FARE);
        assertThat(lastFare.getFare()).isZero();
    }

    @DisplayName("어린이 요금 할인 테스트")
    @Test
    void testChild() {
        AgeDiscountPolicy ageDiscountPolicy = new ChildDiscountPolicy();
        Fare lastFare = ageDiscountPolicy.calculateFare(Fare.BASE_FARE);
        assertThat(lastFare.getFare()).isEqualTo(450);
    }

    @DisplayName("청소년 요금 할인 테스트")
    @Test
    void testYouth() {
        AgeDiscountPolicy ageDiscountPolicy = new YouthDiscountPolicy();
        Fare lastFare = ageDiscountPolicy.calculateFare(Fare.BASE_FARE);
        assertThat(lastFare.getFare()).isEqualTo(720);
    }

    @DisplayName("성인 요금 할인 테스트")
    @Test
    void testAdult() {
        AgeDiscountPolicy ageDiscountPolicy = new AdultDiscountPolicy();
        Fare lastFare = ageDiscountPolicy.calculateFare(Fare.BASE_FARE);
        assertThat(lastFare.getFare()).isEqualTo(1250);
    }
}
