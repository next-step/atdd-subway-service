package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 생성시_0보다_작거나_같을_수_없다(int input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Distance(input));
    }

    @Test
    void 거리를_생성할_수_있다() {
        assertThat(new Distance(1).getDistance()).isEqualTo(1);
    }

    @Test
    void 더할수_있다() {
        Distance distance = new Distance(2).plus(new Distance(1));

        assertThat(distance).isEqualTo(new Distance(3));
    }

    @Test
    void 뺄_수_있다() {
        Distance distance = new Distance(2).minus(new Distance(1));

        assertThat(distance).isEqualTo(new Distance(1));
    }

    @Test
    void 뺄때_결과값이_음수_또는_0이_될_수_없다() {
        Distance one = new Distance(1);
        Distance two = new Distance(2);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> one.minus(two));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> one.minus(one));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 9, 10})
    void 거리가_10키로_이하인_경우에는_기본요금을_반환한다(int distance) {
        assertThat(new Distance(distance).getDistanceFare()).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "49,2050", "50,2050"})
    void 거리가_10키로_초과_50키로_미만인_경우에는_5키로마다_100원이_추가되어_요금을_반환한다(int distance, int expectedDistanceFare) {
        assertThat(new Distance(distance).getDistanceFare()).isEqualTo(expectedDistanceFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    void 거리가_50키로_초과인_경우에는_8키로마다_100원이_추가되어_요금을_반환한다(int distance, int expectedDistanceFare) {
        assertThat(new Distance(distance).getDistanceFare()).isEqualTo(expectedDistanceFare);
    }

}