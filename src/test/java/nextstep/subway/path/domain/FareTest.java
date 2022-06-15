package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FareTest {

    @Test
    void of() {
        // When
        Fare 기본요금 = Fare.of(10);
        Fare 거리_10초과_50이하 = Fare.of(50);
        Fare 거리_50초과 = Fare.of(51);

        // Then
        assertAll(
                () -> assertThat(기본요금.getFare()).isEqualTo(1250),
                () -> assertThat(거리_10초과_50이하.getFare()).isEqualTo(2250),
                () -> assertThat(거리_50초과.getFare()).isEqualTo(2350)
        );
    }
}
