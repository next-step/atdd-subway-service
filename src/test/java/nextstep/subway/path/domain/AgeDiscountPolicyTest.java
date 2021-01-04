package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountPolicyTest {
    @DisplayName("나이에 적합한 할인 정책을 찾을 수 있다.")
    @ParameterizedTest
    @MethodSource("findTestResource")
    void findTest(Integer age, AgeDiscountPolicy expected) {
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.find(age);

        assertThat(ageDiscountPolicy).isEqualTo(expected);
    }
    public static Stream<Arguments> findTestResource() {
        return Stream.of(
                Arguments.of(5, AgeDiscountPolicy.NONE),
                Arguments.of(6, AgeDiscountPolicy.KID),
                Arguments.of(12, AgeDiscountPolicy.KID),
                Arguments.of(13, AgeDiscountPolicy.TEEN),
                Arguments.of(18, AgeDiscountPolicy.TEEN),
                Arguments.of(19, AgeDiscountPolicy.NONE)
        );
    }

    @DisplayName("할인 정책에 맞춰 금액을 할인할 수 있다.")
    @ParameterizedTest
    @MethodSource("applyDiscountTestResource")
    void applyDiscountTest(AgeDiscountPolicy policy, BigDecimal expected) {
        BigDecimal extraFee = BigDecimal.valueOf(2000);

        assertThat(policy.applyDiscount(extraFee)).isEqualTo(expected);
    }
    public static Stream<Arguments> applyDiscountTestResource() {
        return Stream.of(
                Arguments.of(AgeDiscountPolicy.NONE, BigDecimal.valueOf(2000)),
                Arguments.of(AgeDiscountPolicy.TEEN, BigDecimal.valueOf(1320)),
                Arguments.of(AgeDiscountPolicy.KID, BigDecimal.valueOf(825))
        );
    }
}