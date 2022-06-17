package nextstep.subway.member.domain;

import nextstep.subway.exception.InvalidAgeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgeTest {

    @DisplayName("Age 예외 생성")
    @ParameterizedTest
    @ValueSource(ints = { -10, -5, 0, 5 })
    void ageException(int value) {
        assertThatThrownBy(() -> {
            new Age(value);
        }).isInstanceOf(InvalidAgeException.class);
    }

    @DisplayName("Age 정상 생성")
    @ParameterizedTest
    @ValueSource(ints = { 6, 13, 19, 100 })
    void ageNormal(int value) {
        new Age(value);
    }
}
