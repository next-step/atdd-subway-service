package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
    @DisplayName("연령에 따라 할인이 적용된다.")
    @ParameterizedTest
    @CsvSource({
        "5, 0",
        "6, 450",
        "13, 720",
        "19, 1250",
        "65, 0"
    })
    void test(int age, int expected) {
        int fare = 1250;

        AgeFarePolicy ageFarePolicy = AgeFarePolicy.of(age);
        int discount = ageFarePolicy.discount(fare);

        assertThat(discount).isEqualTo(expected);
    }
}
