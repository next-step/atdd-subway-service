package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareRuleTest {

    @ParameterizedTest
    @CsvSource(value = {"13:700", "19:1250", "6:400"}, delimiter = ':')
    void 나이별_요금계산(int age, Long fare) {
        Fare fareDistance = FareRuleAge.calculate(age);
        assertThat(fareDistance).isEqualTo(new Fare(fare));
    }

    @ParameterizedTest
    @CsvSource(value = {"100:12.0", "100:14.0", "100:15.0", "200:16.0",
        "100:58.0"}, delimiter = ':')
    void 거리별_요금계산(Long fare, double distance) {
        Fare fareDistance = FareRuleDistance.calculate(new Distance(distance));
        assertThat(fareDistance).isEqualTo(new Fare(fare));
    }
}
