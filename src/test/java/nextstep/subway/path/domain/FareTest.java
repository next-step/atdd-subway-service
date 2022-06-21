package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Age;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"19,1250", "15,720", "10,450"})
    void 나이대별_요금을_구할_수_있다(int age, int expectedFare) {
        Fare fare = new Fare(new Distance(10), new Age(age), 0);
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"19,2250", "15,1720", "10,1450"})
    void 나이대별_추가요금이_제대로_더해질_수_있다(int age, int expectedFare) {
        Fare fare = new Fare(new Distance(10), new Age(age), 1000);
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "50,2050", "57,2050", "58,2150"})
    void 거리별_요금을_구할_수_있다(int distance, int expectedFare) {
        Fare fare = new Fare(new Distance(distance), new Age(19), 0);
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

}