package nextstep.subway.path.policy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DisplayName("할인 정책에 따른 할인 요금 확인 테스트")
class AgeDiscountPolicyTest {

    @DisplayName("나이에 따른 할인요금 확인")
    @ParameterizedTest(name = "#{index} - {0}의 경우 2000원 요금의 할인 금액은 {2}원이다.")
    @MethodSource("age_discount_policy")
    void age_fare_check(String member, AgeDiscountPolicy ageDiscountPolicy, int discountFare) {

        // when && then
        Assertions.assertThat(ageDiscountPolicy.discount(1250)).isEqualTo(discountFare);
    }

    private static Stream<Arguments> age_discount_policy() {
        return Stream.of(
                Arguments.of("어린이(6~13)", new KidsAgeDiscountPolicy(), 450),
                Arguments.of("청소년(13~19)", new TeenagersAgeDiscountPolicy(), 720),
                Arguments.of("일반인(19~65)", new BasicAgeDiscountPolicy(), 1250)
        );
    }

}
