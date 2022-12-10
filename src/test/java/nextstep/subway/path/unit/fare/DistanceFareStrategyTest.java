package nextstep.subway.path.unit.fare;

import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.DistanceFareStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFareStrategyTest {

    @DisplayName("거리별 추가요금 조회")
    @ParameterizedTest
    @CsvSource(value = {"5:0", "20:200", "60:900"}, delimiter = ':')
    void getAdditionalFare(int distanceInput, int additionalChargeInput) {
        // given
        Distance distance = Distance.from(distanceInput);
        DistanceFareStrategy distanceFareStrategy = new DistanceFareStrategy(distance);

        // when
        int additionalFare = distanceFareStrategy.getAdditionalFare();

        // then
        assertEquals(additionalChargeInput, additionalFare);
    }
}