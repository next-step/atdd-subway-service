package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("거리 요금 관련 도메인 테스트")
class DistanceFeeTypeTest {

    @DisplayName("거리에 따라 요금을 계산할 수 있다.")
    @Test
    void calculateDistanceOverFare() {
        // when
        DistanceFeeType distanceFeeType = DistanceFeeType.getDistanceFeeType(12);
        int fare = DistanceFeeType.calculateOverFare(12, distanceFeeType);


        assertAll(
                () -> assertThat(distanceFeeType).isEqualTo(DistanceFeeType.OVER10),
                () -> assertThat(fare).isEqualTo(1350)
        );
    }

}