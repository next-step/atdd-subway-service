package nextstep.subway.fare;

import nextstep.subway.fare.domain.DistanceFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFareTest {


    @DisplayName("이용거리가 10km이하면 추가요금이 없다")
    @Test
    void findMaxLine() {
        DistanceFare distanceFare = new DistanceFare(8);
        assertThat(distanceFare.getFare()).isEqualTo(1250);
    }

    @DisplayName("이용거리가 10km를 초과하고 50km이하면 5km마다 이용요금이 100원씩 추가된다")
    @Test
    void findFareBetween10to50() {
        DistanceFare distanceFare = new DistanceFare(20);
        assertThat(distanceFare.getFare()).isEqualTo(1450);
    }

    @DisplayName("이용거리가 50km초과시 8km마다 이용요금이 100원씩 추가된다")
    @Test
    void findFareOver50() {
        DistanceFare distanceFare = new DistanceFare(130);
        assertThat(distanceFare.getFare()).isEqualTo(2250);
    }
}

