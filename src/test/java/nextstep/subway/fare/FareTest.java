package nextstep.subway.fare;

import static nextstep.subway.fare.domain.Fare.BASIC_FARE;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.Fare;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

public class FareTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    void 기본_요금_생성(int distance) {
        Fare fare = new Fare(distance, 0);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE);
    }

    @Test
    void 기본_요금과_노선_추가_요금_생성() {
        Fare fare = new Fare(10, 900);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE + 900);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 13, 15, 20, 30})
    void 기본_요금과_10km_초과_거리_추가_요금_생성(int distance) {
        Fare fare = new Fare(distance, 0);
        int additionalFare = (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE + additionalFare);
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 55, 60, 70, 80})
    void 기본_요금과_50km_초과_거리_추가_요금_생성(int distance) {
        Fare fare = new Fare(distance, 0);
        int additionalFare = (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        assertThat(fare.getFare()).isEqualTo(BASIC_FARE + additionalFare);
    }
}
