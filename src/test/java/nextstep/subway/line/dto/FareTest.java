package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Surcharge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("거리별 총 요금 조회 - 기본요금")
    @ParameterizedTest
    @CsvSource({
            "1,1250, 0", "10,1250, 0"
    })
    void distanceFareDefault(int distance, int fare, int surcharge) {
        Fare actual = Fare.ofByDistance(distance, new Surcharge(surcharge));

        assertThat(actual).isEqualTo(new Fare(fare));
    }

    @DisplayName("거리별 총 요금 조회 - 10초과")
    @ParameterizedTest
    @CsvSource({
            "11,1350, 0", "50,2050, 0"
    })
    void distanceFareTenOver(int distance, int fare, int surcharge) {
        Fare actual = Fare.ofByDistance(distance, new Surcharge(surcharge));

        assertThat(actual).isEqualTo(new Fare(fare));
    }

    @DisplayName("거리별 총 요금 조회 - 50초과")
    @ParameterizedTest
    @CsvSource({
            "51,2150, 0", "100,2750, 0"
    })
    void distanceFareFiftyOver(int distance, int fare, int surcharge) {
        Fare actual = Fare.ofByDistance(distance, new Surcharge(surcharge));

        assertThat(actual).isEqualTo(new Fare(fare));
    }

    @DisplayName("부가 요금이 추가된 요금 조회")
    @ParameterizedTest
    @CsvSource({
            "51,2250, 100", "100,3750, 1000"
    })
    void additionalSurchargeTest(int distance, int fare, int surcharge) {
        Fare actual = Fare.ofByDistance(distance, new Surcharge(surcharge));

        assertThat(actual).isEqualTo(new Fare(fare));
    }

}