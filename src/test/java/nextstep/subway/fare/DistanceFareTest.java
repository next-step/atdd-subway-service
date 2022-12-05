package nextstep.subway.fare;

import nextstep.subway.fare.domain.DistanceFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFareTest {


    @DisplayName("이용거리가 10km이하면 추가요금이 없다")
    @ParameterizedTest
    @ValueSource(ints = {1000, 2000, 3000, 4000, 5000})
    void findMaxLine(int fare) {
        DistanceFare distanceFare = new DistanceFare(8);
        assertThat(distanceFare.getFare(fare)).isEqualTo(fare);
    }

    @DisplayName("이용거리가 10km를 초과하고 50km이하면 5km마다 이용요금이 100원씩 추가된다")
    @ParameterizedTest
    @ValueSource(ints = {1000, 2000, 3000, 4000, 5000})
    void findFareBetween10to50(int fare) {
        DistanceFare distanceFare = new DistanceFare(20);
        assertThat(distanceFare.getFare(fare)).isEqualTo(fare + 200);
    }

    @DisplayName("이용거리가 50km초과시 8km마다 이용요금이 100원씩 추가된다")
    @ParameterizedTest
    @ValueSource(ints = {1000, 2000, 3000, 4000, 5000})
    void findFareOver50(int fare) {
        DistanceFare distanceFare = new DistanceFare(130);
        assertThat(distanceFare.getFare(fare)).isEqualTo(fare + 1000);
    }
}

