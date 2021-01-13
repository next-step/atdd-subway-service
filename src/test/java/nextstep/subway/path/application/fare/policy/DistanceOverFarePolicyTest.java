package nextstep.subway.path.application.fare.policy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceOverFarePolicyTest {

    @ParameterizedTest
    @CsvSource({"10,1250", "11,1350", "15,1350", "16,1450", "20,1450", "21,1550", "25,1550", "26,1650",
            "50,2050", "51,2150", "58,2150", "59,2250", "66,2250", "67,2350"})
    void calculateFare(int distance, int expectedFare) {
        //given
        //when
        int fare = DistanceOverFarePolicy.calculateFare(distance);
        //then
        assertThat(fare).isEqualTo(expectedFare);
    }
}