package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FareAgeTest {

    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void isTeenager(final int age) {
        // when
        final FareAge fareAge = new FareAge(age);

        // then
        assertAll(
            () -> assertThat(fareAge.isTeenager()).isTrue(),
            () -> assertThat(fareAge.isChild()).isFalse()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void applyDiscount_notApplicable(final Integer age) {
        // when
        final FareAge fareAge = new FareAge(age);

        // then
        assertAll(
            () -> assertThat(fareAge.isChild()).isTrue(),
            () -> assertThat(fareAge.isTeenager()).isFalse()
        );
    }
}
