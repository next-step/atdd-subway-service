package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareAgePolicyTypeTest {
    @DisplayName("올바른 Type 생성 테스트")
    @ParameterizedTest(name = "올바른 Type 생성: age = {0}, 생성타입 = {1}")
    @MethodSource("provideParametersForFareAgeTypeCreate")
    void FareAgePolicyType_생성(int age, FareAgePolicyType type){
        FareAgePolicyType createdType = FareAgePolicyType.of(age);
        assertThat(createdType.equals(type)).isTrue();
    }

    @DisplayName("Type 별 요금 할인 테스트")
    @ParameterizedTest(name = "Type 별 요금 할인: age = {0}, 기존요금 = {1}, 할인된요금 = {2}")
    @MethodSource("provideParametersForFareAgeTypeCalculate")
    void FareAgePolicyType_요금_계산(int age, int fare, int discountedFare){
        int calculatedFare = FareAgePolicyType.of(age).discountFare(fare);
        assertThat(calculatedFare).isEqualTo(discountedFare);
    }

    private static Stream<Arguments> provideParametersForFareAgeTypeCreate() {
        return Stream.of(
                Arguments.of(0, FareAgePolicyType.BABY_AGE),
                Arguments.of(5, FareAgePolicyType.BABY_AGE),
                Arguments.of(6, FareAgePolicyType.CHILD_AGE),
                Arguments.of(12, FareAgePolicyType.CHILD_AGE),
                Arguments.of(13, FareAgePolicyType.TEENAGER_AGE),
                Arguments.of(18, FareAgePolicyType.TEENAGER_AGE),
                Arguments.of(19, FareAgePolicyType.ADULT_AGE)
        );
    }

    private static Stream<Arguments> provideParametersForFareAgeTypeCalculate() {
        return Stream.of(
                Arguments.of(5, 2000, 0),
                Arguments.of(12, 2000, 825),
                Arguments.of(18, 2000, 1320),
                Arguments.of(19, 2000, 2000)
        );
    }
}