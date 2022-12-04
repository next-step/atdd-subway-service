package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리 관련 도메인 테스트")
public class DistanceTest {

    @ParameterizedTest(name = "길이 생성 시, 길이가 0보다 작으면 에러가 발생한다.(distance: {0})")
    @ValueSource(ints = {-10, -1, -5})
    void createDistanceThrowErrorWhenDistanceLessThenZero(int distance) {
        // when & then
        assertThatThrownBy(() -> Distance.from(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.거리는_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("길이를 생성하면 조회할 수 있다.")
    @Test
    void createDistance() {
        // given
        int actual = 10;

        // when
        Distance distance = Distance.from(actual);

        // then
        assertThat(distance.value()).isEqualTo(actual);
    }

    @DisplayName("길이에서 길이를 빼면 새로운 길이가 나온다.")
    @Test
    void subtractDistance() {
        // given
        int original = 15;
        int target = 5;
        Distance distance = Distance.from(original);
        Distance subtractDistance = Distance.from(target);

        // when
        Distance resultDistance = distance.subtract(subtractDistance);

        // then
        assertThat(resultDistance.value()).isEqualTo(original - target);
    }

    @DisplayName("길이에서 int형 길이를 빼면 새로운 길이가 나온다.")
    @Test
    void subtractDistance2() {
        // given
        int original = 15;
        int target = 5;
        Distance distance = Distance.from(original);

        // when
        Distance resultDistance = distance.subtract(target);

        // then
        assertThat(resultDistance.value()).isEqualTo(original - target);
    }

    @DisplayName("기존 길이보다 큰 길이를 빼려고 하면 에러가 발생한다.")
    @Test
    void subtractDistanceThrowErrorWhenOriginalSmallerThenTarget() {
        // given
        int original = 5;
        int target = 10;
        Distance distance = Distance.from(original);
        Distance subtractDistance = Distance.from(target);

        // when & then
        assertThatThrownBy(() -> distance.subtract(subtractDistance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.거리는_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("길이에서 길이를 더하면 새로운 길이가 나온다.")
    @Test
    void addDistance() {
        // given
        int original = 15;
        int add = 5;
        Distance distance = Distance.from(original);
        Distance addDistance = Distance.from(add);

        // when
        Distance resultDistance = distance.add(addDistance);

        // then
        assertThat(resultDistance.value()).isEqualTo(original + add);
    }

    @ParameterizedTest(name = "주어진 길이({1})보다 값({0})이 크면 참을 반환한다.")
    @CsvSource(value = {"30:29", "30:25", "40:35"}, delimiter = ':')
    void isBiggerThanOtherDistance(int actual, int compare) {
        // given
        Distance distance = Distance.from(actual);

        // when
        boolean isBiggerThan = distance.isBiggerThen(Distance.from(compare));

        // then
        assertAll(
                () -> assertThat(isBiggerThan).isTrue(),
                () -> assertThat(isBiggerThan).isEqualTo(actual > compare)
        );
    }

    @ParameterizedTest(name = "주어진 길이({1})보다 값({0})이 작거나 같으면 거짓을 반환한다.")
    @CsvSource(value = {"20:29", "30:30", "20:35"}, delimiter = ':')
    void isEqualOrSmallerThanOtherDistance(int actual, int compare) {
        // given
        Distance distance = Distance.from(actual);

        // when
        boolean isBiggerThan = distance.isBiggerThen(Distance.from(compare));

        // then
        assertAll(
                () -> assertThat(isBiggerThan).isFalse(),
                () -> assertThat(isBiggerThan).isEqualTo(actual > compare)
        );
    }

    @ParameterizedTest(name = "{0}을 {1}로 나누면 올림 처리된 값인 {2}를 반환한다.")
    @CsvSource(value = {"1000:10:100", "234:10:24", "122:10:13", "147:10:15"}, delimiter = ':')
    void divideAndCeil(int number, int divideNumber, int expect) {
        // when & then
        assertThat(Distance.from(number).divideAndCeil(Distance.from(divideNumber))).isEqualTo(expect);
    }
}
