package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리 요금 정책")
class FareDistancePolicyTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> FareDistancePolicy.from(Distance.from(10)));
    }

    @Test
    @DisplayName("계산할 거리는 필수")
    void instance_nullArgument_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> FareDistancePolicy.from(null))
            .withMessageEndingWith("필수입니다.");
    }


    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 기본요금")
    @DisplayName("이동거리가 10km 이하라면 기본 요금 1250원")
    @ValueSource(ints = {1, 5, 10})
    void fare_defaultFare(int distance) {
        // given
        FareDistancePolicy policy = FareDistancePolicy.from(Distance.from(distance));

        // when
        Fare fare = policy.fare();

        // then
        assertThat(fare).isEqualTo(Fare.from(1250));
    }

    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 요금은 {1}")
    @DisplayName("이동거리가 50km 이하라면 5km 당 100원씩 추가되어야 한다")
    @CsvSource({"11,1350", "20,1450", "50,2050"})
    void fare_shortDistance(int distance, int expectedFare) {
        // given
        FareDistancePolicy policy = FareDistancePolicy.from(Distance.from(distance));

        // when
        Fare fare = policy.fare();

        // then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }

    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 요금은 {1}")
    @DisplayName("이동거리가 50km 초과라면 8km 당 100원씩 추가되어야 한다")
    @CsvSource({"58,2150", "66,2250", "74,2350"})
    void fare_farDistance(int distance, int expectedFare) {
        // given
        FareDistancePolicy policy = FareDistancePolicy.from(Distance.from(distance));

        // when
        Fare fare = policy.fare();

        // then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }
}
