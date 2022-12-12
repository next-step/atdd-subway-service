package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Distance;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "12,1350", "13,1350", "14,1350", "15,1350", "16,1450", "20,1450",
        "50,2050", "51,2850", "55,2850", "58,2850", "59,3650", "66,3650", "67,4450"
    })
    void fare1(int distance, int fare) {
        assertThat(new Fare(new Distance(distance))).isEqualTo(new Fare(fare));
    }
}
