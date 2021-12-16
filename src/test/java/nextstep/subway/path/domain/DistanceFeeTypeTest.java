package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("거리 요금 관련 도메인 테스트")
class DistanceFeeTypeTest {

    @DisplayName("10km 이내 거리는 1250원만 낸다.")
    @Test
    void calculateDistanceOverFareUnder10() {
        // when
        DistanceFeeType distanceFeeType = DistanceFeeType.getDistanceFeeType(8);
        int fare = DistanceFeeType.calculateOverFare(8, distanceFeeType);


        assertAll(
                () -> assertThat(distanceFeeType).isEqualTo(DistanceFeeType.NONE),
                () -> assertThat(fare).isEqualTo(1250)
        );
    }

    @DisplayName("10km 이하의 거리는 5km 당 100원을 낸다.")
    @Test
    void calculateDistanceOverFareOver10() {
        // when
        DistanceFeeType distanceFeeType = DistanceFeeType.getDistanceFeeType(17);
        int fare = DistanceFeeType.calculateOverFare(17, distanceFeeType);


        assertAll(
                () -> assertThat(distanceFeeType).isEqualTo(DistanceFeeType.OVER10),
                () -> assertThat(fare).isEqualTo(1450)
        );
    }

    @DisplayName("50km 초과의 거리는 8km마다 100원을 낸다.")
    @Test
    void calculateDistanceOverFareOver50() {
        // when
        DistanceFeeType distanceFeeType = DistanceFeeType.getDistanceFeeType(54);
        int fare = DistanceFeeType.calculateOverFare(54, distanceFeeType);


        assertAll(
                () -> assertThat(distanceFeeType).isEqualTo(DistanceFeeType.OVER50),
                () -> assertThat(fare).isEqualTo(1850)
        );
    }

}