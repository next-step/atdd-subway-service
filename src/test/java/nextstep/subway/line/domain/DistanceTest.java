package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.common.exception.SubwayException;

public class DistanceTest {
    @DisplayName("거리의 길이를 0 이하로 생성하면 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void construct_throwErrorWhenLowerThanOne(int distance) {
        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> new Distance(distance))
            .withMessage("1 이상의 길이만 입력 가능합니다.");
    }

    @DisplayName("거리 더하기")
    @Test
    void add() {
        Distance result = new Distance(10)
            .add(new Distance(5));

        assertThat(result)
            .isEqualTo(new Distance(15));
    }

    @DisplayName("거리 빼기")
    @Test
    void subtract() {
        Distance result = new Distance(10)
            .subtract(new Distance(5));

        assertThat(result)
            .isEqualTo(new Distance(5));
    }

    @DisplayName("거리가 작거나 같은지 확인")
    @ParameterizedTest
    @CsvSource(value = {"9,false", "10,true", "11,true"})
    void isLowerOrEqualThan(int distance, boolean expected) {
        boolean result = new Distance(10)
            .isLowerOrEqualThan(new Distance(distance));

        assertThat(result)
            .isEqualTo(expected);
    }
}
