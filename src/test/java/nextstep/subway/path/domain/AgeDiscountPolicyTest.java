package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
}