package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AgeDiscountPolicy 클래스 테스트")
class AgeDiscountPolicyTest {

    @DisplayName("연령에 따라 요금 할인을 받는다")
    @ParameterizedTest
    @CsvSource({
            "5, 0",
            "6, 450",
            "13, 720",
            "19, 1250",
            "65, 0"
    })
    void test(int age, int expected) {
        Fare fare = new Fare(1250);

        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(age);
        Fare discount = ageDiscountPolicy.discount(fare);

        assertThat(discount.getValue()).isEqualTo(expected);
    }
}
