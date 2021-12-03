package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("퍼센트")
class PercentTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Percent.from(50));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @ValueSource(ints = {-1, 101})
    @DisplayName("0부터 100까지만 값을 넣을 수 있다.")
    void instance_outOfRange_thrownIllegalArgumentException(int value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Percent.from(value))
            .withMessageEndingWith("까지만 가능합니다.");
    }

    @ParameterizedTest(name = "[{index}] 1000 에 {0} 퍼센트를 적용하면 {1}")
    @CsvSource({"0,0", "30,300", "50,500", "70,700", "100, 1000"})
    @DisplayName("퍼센트 계산")
    void percentageOf(int percent, int expected) {
        // when
        int actual = Percent.from(percent).percentageOf(1000);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
