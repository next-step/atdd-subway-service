package nextstep.subway.fare;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.fare.FareAgeRule.fareByDiscount;
import static nextstep.subway.fare.FareAgeRule.fareByAge;
import static org.assertj.core.api.Assertions.assertThat;

class FareAgeRuleTest {

    private final int FARE = 1000;

    @DisplayName("나이에 따른 요금할인 : 13~19세미만(20%), 6~13미만(50%)")
    @ParameterizedTest
    @CsvSource(value = {"30:1000","13:520","8:325"}, delimiter = ':')
    void discountFareByAge(int age, int expected) {
        Optional<FareAgeRule> fareAgeRule = fareByAge(age);
        assertThat(fareByDiscount(fareAgeRule, FARE)).isEqualTo(expected);
    }
}