package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.policy.discount.ChildrenDiscountPolicy;
import nextstep.subway.path.domain.policy.discount.TeenagerDiscountPolicy;
import nextstep.subway.path.domain.policy.distance.DefaultDistancePolicy;
import nextstep.subway.path.domain.policy.distance.OverFiftyPolicy;
import nextstep.subway.path.domain.policy.distance.OverTenAndUnderFiftyPolicy;
import nextstep.subway.path.domain.policy.line.AdditionalLineFarePolicy;
import nextstep.subway.path.domain.policy.line.DefaultLineFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PoliciesTest {

    private static final int BASIC_FARE = 1250;

    @DisplayName("50킬로 넘고, 라인 추가 요금 있을 때 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"53:300:2450", "58:500:2650", "67:100:2450"},  delimiter = ':')
    void calculate(int distance, int lineFare, int expectedFare) {
        // given
        OverFiftyPolicy distancePolicy = new OverFiftyPolicy(distance);
        AdditionalLineFarePolicy linePolicy = new AdditionalLineFarePolicy(lineFare);
        Policies policies = Policies.of(distancePolicy, linePolicy);

        // when
        int calculated = policies.calculate(BASIC_FARE);

        // then
        assertThat(calculated).isEqualTo(expectedFare);
    }

    @DisplayName("10킬로 넘고, 라인 추가 요금 있을 때 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"12:300:1650", "16:500:1950", "25:100:1650"},  delimiter = ':')
    void calculate2(int distance, int lineFare, int expectedFare) {
        // given
        OverTenAndUnderFiftyPolicy distancePolicy = new OverTenAndUnderFiftyPolicy(distance);
        AdditionalLineFarePolicy linePolicy = new AdditionalLineFarePolicy(lineFare);
        Policies policies = Policies.of(distancePolicy, linePolicy);

        // when
        int calculated = policies.calculate(BASIC_FARE);

        // then
        assertThat(calculated).isEqualTo(expectedFare);
    }

    @DisplayName("기본거리에 , 라인 추가 요금 있을 때 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {"300:1550", "500:1750", "100:1350"},  delimiter = ':')
    void calculate3(int lineFare, int expectedFare) {
        // given
        DefaultDistancePolicy distancePolicy = new DefaultDistancePolicy();
        AdditionalLineFarePolicy linePolicy = new AdditionalLineFarePolicy(lineFare);
        Policies policies = Policies.of(distancePolicy, linePolicy);

        // when
        int calculated = policies.calculate(BASIC_FARE);

        // then
        assertThat(calculated).isEqualTo(expectedFare);
    }

    @DisplayName("기본 거리에 , 라인 추가 요금 없을 때 요금을 계산한다")
    @Test
    void calculate4() {
        // given
        DefaultDistancePolicy distancePolicy = new DefaultDistancePolicy();
        DefaultLineFarePolicy linePolicy = new DefaultLineFarePolicy();
        Policies policies = Policies.of(distancePolicy, linePolicy);

        // when
        int calculated = policies.calculate(BASIC_FARE);

        // then
        assertThat(calculated).isEqualTo(1250);
    }

    @DisplayName("기본 거리에 , 라인 추가 요금 없을 때 요금을 계산하고, 청소년 할인을 받는다.")
    @Test
    void calculate5() {
        // given
        DefaultDistancePolicy distancePolicy = new DefaultDistancePolicy();
        DefaultLineFarePolicy linePolicy = new DefaultLineFarePolicy();
        TeenagerDiscountPolicy discountPolicy = new TeenagerDiscountPolicy();
        Policies policies = Policies.of(distancePolicy, linePolicy, discountPolicy);

        // when
        int calculated = policies.calculate(BASIC_FARE);

        // then
        assertThat(calculated).isEqualTo(1070);
    }

    @DisplayName("기본 거리에 , 라인 추가 요금 없을 때 요금을 계산하고, 어린이 할인을 받는다.")
    @Test
    void calculate6() {
        // given
        DefaultDistancePolicy distancePolicy = new DefaultDistancePolicy();
        DefaultLineFarePolicy linePolicy = new DefaultLineFarePolicy();
        ChildrenDiscountPolicy discountPolicy = new ChildrenDiscountPolicy();
        Policies policies = Policies.of(distancePolicy, linePolicy, discountPolicy);

        // when
        int calculated = policies.calculate(BASIC_FARE);

        // then
        assertThat(calculated).isEqualTo(800);
    }
}
