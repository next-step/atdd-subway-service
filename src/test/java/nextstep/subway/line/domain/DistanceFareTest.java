package nextstep.subway.line.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFareTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 9, 10})
    void 거리가_10키로_이하인_경우에는_기본요금을_반환한다(int distance) {
        assertThat(new DistanceFare(distance).getFare()).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "49,2050", "50,2050"})
    void 거리가_10키로_초과_50키로_미만인_경우에는_5키로마다_100원이_추가되어_요금을_반환한다(int distance, int expectedDistanceFare) {
        assertThat(new DistanceFare(distance).getFare()).isEqualTo(expectedDistanceFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"51,2050", "57,2050", "58,2150", "59,2150", "65,2150", "66,2250"})
    void 거리가_50키로_초과인_경우에는_8키로마다_100원이_추가되어_요금을_반환한다(int distance, int expectedDistanceFare) {
        assertThat(new DistanceFare(distance).getFare()).isEqualTo(expectedDistanceFare);
    }

}