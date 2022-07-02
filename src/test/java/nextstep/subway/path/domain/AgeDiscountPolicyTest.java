package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountPolicyTest {

    @DisplayName("연령별 할인 정책 적용")
    @ParameterizedTest
    @CsvSource({
            "5, 0",
            "6, 450",
            "13, 720",
            "19, 900",
    })
    void discount(int age, int expected) {
        // given
        Fare fare = new Fare(1250);

        // when
        Fare actual = AgeDiscountPolicy.discount(fare, age);

        // then
        assertThat(actual).isEqualTo(new Fare(expected));
    }
}