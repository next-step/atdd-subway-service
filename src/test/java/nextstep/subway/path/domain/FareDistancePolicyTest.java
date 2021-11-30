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


    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 요금은 {1}")
    @DisplayName("거리별 요금 계산")
    @CsvSource({"10,1250", "11,1350", "20,1450", "50,2050", "58,2150", "66,2250"})
    void fare(int distance, int expectedFare) {
        //given
        FareDistancePolicy policy = FareDistancePolicy.from(Distance.from(distance));

        //when
        Fare fare = policy.fare();

        //then
        assertThat(fare).isEqualTo(Fare.from(expectedFare));
    }
}
