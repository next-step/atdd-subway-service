package nextstep.subway.line.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("거리별 총 요금 조회 - 기본요금")
    @ParameterizedTest
    @CsvSource({
            "1,1250", "10,1250"
    })
    void distanceFareDefault(int distance, int fare) {
        Fare actual = Fare.ofByDistance(distance);

        assertThat(actual).isEqualTo(new Fare(fare));
    }

    @DisplayName("거리별 총 요금 조회 - 10초과")
    @ParameterizedTest
    @CsvSource({
            "11,1350", "50,2050"
    })
    void distanceFareTenOver(int distance, int fare) {
        Fare actual = Fare.ofByDistance(distance);

        assertThat(actual).isEqualTo(new Fare(fare));
    }

    @DisplayName("거리별 총 요금 조회 - 50초과")
    @ParameterizedTest
    @CsvSource({
            "51,2150", "100,2750"
    })
    void distanceFareFiftyOver(int distance, int fare) {
        Fare actual = Fare.ofByDistance(distance);

        assertThat(actual).isEqualTo(new Fare(fare));
    }

}