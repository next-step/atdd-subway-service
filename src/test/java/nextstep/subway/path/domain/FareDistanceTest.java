package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource(value = {"0,0", "10,0"})
    void calculateDistanceUnit_short(final int distance, final int expectedUnit) {
        // given
        final FareDistance fareDistance = new FareDistance(distance);

        // when
        final int actualUnit = fareDistance.calculateDistanceUnit();

        // then
        assertThat(actualUnit).isEqualTo(expectedUnit);
    }

    @ParameterizedTest
    @CsvSource(value = {"11,0", "15,1", "50,8"})
    void calculateDistanceUnit_mid(final int distance, final int expectedUnit) {
        // given
        final FareDistance fareDistance = new FareDistance(distance);

        // when
        final int actualUnit = fareDistance.calculateDistanceUnit();

        // then
        assertThat(actualUnit).isEqualTo(expectedUnit);
    }

    @ParameterizedTest
    @CsvSource(value = {"51,0", "58,1"})
    void calculateDistanceUnit_long(final int distance, final int expectedUnit) {
        // given
        final FareDistance fareDistance = new FareDistance(distance);

        // when
        final int actualUnit = fareDistance.calculateDistanceUnit();

        // then
        assertThat(actualUnit).isEqualTo(expectedUnit);
    }
}
