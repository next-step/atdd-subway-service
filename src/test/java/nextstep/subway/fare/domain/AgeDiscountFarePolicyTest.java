package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountFarePolicyTest {

    @DisplayName("주어진 나이에 해당되는 요금 할인 정책을 반환한다")
    @ParameterizedTest(name = "[{0}] 나이가 주어지면 [{1}] 요금 할인 정책을 반환한다")
    @MethodSource("provideAgeDiscountFarePolicy")
    void find_age_discount_fare_policy_with_age(int age, AgeDiscountFarePolicy expectedFarePolicy) {
        // when
        AgeDiscountFarePolicy policy = AgeDiscountFarePolicy.of(age);

        // then
        assertThat(policy).isEqualTo(expectedFarePolicy);
    }

    private static Stream<Arguments> provideAgeDiscountFarePolicy() {
        return Stream.of(
                Arguments.of(6, AgeDiscountFarePolicy.CHILDREN),
                Arguments.of(12, AgeDiscountFarePolicy.CHILDREN),
                Arguments.of(13, AgeDiscountFarePolicy.TEENAGER),
                Arguments.of(18, AgeDiscountFarePolicy.TEENAGER),
                Arguments.of(19, AgeDiscountFarePolicy.NORMAL),
                Arguments.of(22, AgeDiscountFarePolicy.NORMAL)
        );
    }

    @DisplayName("주어진 나이에 해당되는 할인 정책이 적용 된 요금을 계산하여 반환한다")
    @ParameterizedTest(name = "[{0}] 나이가 주어지면 요금 할인 정책이 적용 된 요금 [{1}원]을 반환한다")
    @MethodSource("provideAgeAndDiscountFare")
    void calculate_discount_fare(int age, Fare fare, Fare expectedFare) {
        // given
        AgeDiscountFarePolicy policy = AgeDiscountFarePolicy.of(age);

        // when
        Fare discountedFare = policy.apply(fare);

        // then
        assertThat(discountedFare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> provideAgeAndDiscountFare() {
        return Stream.of(
                Arguments.of(6, Fare.of(1_250), Fare.of(450)),
                Arguments.of(12, Fare.of(1_550), Fare.of(600)),
                Arguments.of(12, Fare.of(2_150), Fare.of(900)),
                Arguments.of(13, Fare.of(1_250), Fare.of(720)),
                Arguments.of(18, Fare.of(1_550), Fare.of(960)),
                Arguments.of(18, Fare.of(2_150), Fare.of(1_440)),
                Arguments.of(19, Fare.of(1_250), Fare.of(1_250)),
                Arguments.of(22, Fare.of(1_550), Fare.of(1_550)),
                Arguments.of(22, Fare.of(2_150), Fare.of(2_150))
        );
    }
}
