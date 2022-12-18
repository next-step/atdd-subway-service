package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("지하철 노선 거리 밸류 모델 단위 테스트")
class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -10})
    void invalidValue(final int value) {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Distance.from(value));
    }

    @ParameterizedTest
    @CsvSource(value = {"0:0:0", "1:2:3", "5:10:15",}, delimiter = ':')
    void plus(final int first, final int second, final int result) {
        assertThat(Distance.from(first).plus(Distance.from(second)))
            .isEqualTo(Distance.from(result));

        assertThat(Distance.from(second).plus(Distance.from(first)))
            .isEqualTo(Distance.from(result));
    }

    @ParameterizedTest
    @CsvSource(value = {"0:0:0", "1:1:0", "2:1:1",}, delimiter = ':')
    void minus(final int first, final int second, final int result) {
        assertThat(Distance.from(first).minus(Distance.from(second)))
            .isEqualTo(Distance.from(result));
    }

    @ParameterizedTest
    @CsvSource(value = {"0:1", "1:2"}, delimiter = ':')
    void isLessThanOrEqual_differentValue(final int smallerValue, final int value) {
        assertThat(Distance.from(smallerValue).isLessThanOrEqual(Distance.from(value)))
            .isTrue();
        assertThat(Distance.from(value).isLessThanOrEqual(Distance.from(smallerValue)))
            .isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    void isLessThanOrEqual_sameValue(final int value) {
        assertThat(Distance.from(value).isLessThanOrEqual(Distance.from(value)))
            .isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"0:1", "1:2"}, delimiter = ':')
    void isGreaterThan_differentValue(final int smallerValue, final int value) {
        assertThat(Distance.from(smallerValue).isGreaterThan(Distance.from(value)))
            .isFalse();
        assertThat(Distance.from(value).isGreaterThan(Distance.from(smallerValue)))
            .isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    void isGreaterThan_sameValue(final int value) {
        assertThat(Distance.from(value).isGreaterThan(Distance.from(value)))
            .isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10})
    void equals(final int value) {
        assertThat(Distance.from(value))
            .isEqualTo(Distance.from(value));
    }

}
