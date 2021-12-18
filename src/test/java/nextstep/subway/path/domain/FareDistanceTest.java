package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class FareDistanceTest {

    @Test
    void create_invalidDistance() {
        // given
        final int invalidDistance = -1;

        // when, then
        assertThatThrownBy(
            () -> new FareDistance(invalidDistance)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void calculateDistanceUnit_short() {
        // given
        final FareDistance distance = new FareDistance(10);

        // when
        final int unit = distance.calculateDistanceUnit();

        // then
        assertThat(unit).isEqualTo(0);
    }

    @Test
    void calculateDistanceUnit_mid() {
        // given
        final FareDistance distance = new FareDistance(24);

        // when
        final int unit = distance.calculateDistanceUnit();

        // then
        assertThat(unit).isEqualTo(2);
    }

    @Test
    void calculateDistanceUnit_long() {
        // given
        final FareDistance distance = new FareDistance(64);

        // when
        final int unit = distance.calculateDistanceUnit();

        // then
        assertThat(unit).isEqualTo(1);
    }
}
