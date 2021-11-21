package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리")
class DistanceTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Distance.from(Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("음수로 객체화")
    void instance_negativeValue_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Distance.from(Integer.MIN_VALUE))
            .withMessageEndingWith("distance value must be positive");
    }

    @Test
    @DisplayName("더하기")
    void sum() {
        // given
        Distance tenDistance = Distance.from(10);

        // when
        Distance sum = tenDistance.sum(Distance.from(10));

        // then
        assertThat(sum)
            .isEqualTo(Distance.from(20));
    }

    @Test
    @DisplayName("빼기")
    void subtract() {
        // given
        Distance distance = Distance.from(10);

        // when
        Distance subtractedDistance = distance.subtract(Distance.from(3));

        // then
        assertThat(subtractedDistance)
            .isEqualTo(Distance.from(7));
    }

    @ParameterizedTest(name = "[{index}] 10인 거리에서 {0}인 거리를 뺄 수 없다.")
    @ValueSource(ints = {10, Integer.MAX_VALUE})
    @DisplayName("같거나 더 큰수로 뺀다.")
    void subtract_equalOrGreaterThan_thrownIllegalArgumentException(Integer distance) {
        // given
        Distance tenDistance = Distance.from(10);

        // when
        ThrowingCallable subtractCall = () -> tenDistance.subtract(Distance.from(distance));

        // then
        assertThatIllegalArgumentException()
            .isThrownBy(subtractCall)
            .withMessageEndingWith("distance value must be positive");
    }

    @ParameterizedTest(name = "[{index}] 10인 거리가 {0} 보다 이상이라는 사실은 {1}")
    @CsvSource({"9,false", "10,true", "11,true"})
    @DisplayName("값 이하 또는 동일 여부 판단")
    void lessThanOrEqual(Integer distance, boolean expected) {
        // given
        Distance tenDistance = Distance.from(10);

        // when
        boolean moreThan = tenDistance.lessThanOrEqual(Distance.from(distance));

        // then
        assertThat(moreThan)
            .isEqualTo(expected);
    }
}
