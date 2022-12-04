package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.DistanceFarePolicy.findDistanceFarePolicy;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.DistanceFarePolicy;
import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리별 요금 정책 도메인 테스트")
class DistanceFarePolicyTest {

    @ParameterizedTest(name = "거리가 50km를 초과하면 FIFTY_KM 요금 정책을 가진다. (distance: {0})")
    @ValueSource(ints = {51, 52, 80, 100})
    void isFiftyKmPolicy(int distance) {
        // when
        DistanceFarePolicy distanceFarePolicy = findDistanceFarePolicy(Distance.from(distance));

        // then
        assertThat(distanceFarePolicy).isEqualTo(DistanceFarePolicy.FIFTY_KM);
    }

    @ParameterizedTest(name = "거리가 10km를 초과하고, 50km 이하이면 TEN_KM 요금 정책을 가진다. (distance: {0})")
    @ValueSource(ints = {11, 22, 36, 50})
    void isTenKmPolicy(int distance) {
        // when
        DistanceFarePolicy distanceFarePolicy = findDistanceFarePolicy(Distance.from(distance));

        // then
        assertThat(distanceFarePolicy).isEqualTo(DistanceFarePolicy.TEN_KM);
    }

    @ParameterizedTest(name = "거리가 10km 이하이면 ZERO_KM 요금 정책을 가진다. (distance: {0})")
    @ValueSource(ints = {1, 3, 5, 10})
    void isZeroKmPolicy(int distance) {
        // when
        DistanceFarePolicy distanceFarePolicy = findDistanceFarePolicy(Distance.from(distance));

        // then
        assertThat(distanceFarePolicy).isEqualTo(DistanceFarePolicy.ZERO_KM);
    }

    @ParameterizedTest(name = "거리가 {0}km이면, 추가 요금이 {1}원이다. (성인 요금제 기준)")
    @CsvSource(value = {"30:400", "85:1300", "162:2200", "110:1600"}, delimiter = ':')
    void calculateAdditionalFareOfDistance(int number, int additionalFare) {
        // given
        Distance distance = Distance.from(number);
        DistanceFarePolicy distanceFarePolicy = findDistanceFarePolicy(distance);

        // when
        Fare fare = distanceFarePolicy.calculateAdditionalFareOfDistance(distance);

        // then
        assertThat(fare).isEqualTo(Fare.from(additionalFare));
    }
}
