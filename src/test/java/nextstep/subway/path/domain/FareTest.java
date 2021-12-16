package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("요금 더하기")
    @Test
    void add() {
        Fare fare = Fare.from(1);

        assertThat(fare.add(Fare.from(2))).isEqualTo(Fare.from(3));
    }
}