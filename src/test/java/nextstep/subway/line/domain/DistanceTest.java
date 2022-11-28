package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리 관련 도메인 테스트")
public class DistanceTest {

    @ParameterizedTest(name = "길이 생성 시, 길이가 0보다 작거나 같으면 에러가 발생한다.(distance: {0})")
    @ValueSource(ints = {0, -1, -5})
    void createDistanceThrowErrorWhenDistanceLessThenOrEqualToZero(int distance) {
        // when & then
        assertThatThrownBy(() -> Distance.from(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
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
                .hasMessage(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
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

    @DisplayName("주어진 길이보다 크면 참을 반환한다.")
    @Test
    void isBiggerThanOtherDistance() {
        // given
        int actual = 50;
        int expect = 40;
        Distance distance = Distance.from(actual);

        // when
        boolean isBiggerThan = distance.isBiggerThan(Distance.from(expect));

        // then
        assertAll(
                () -> assertThat(isBiggerThan).isTrue(),
                () -> assertThat(isBiggerThan).isEqualTo(actual > expect)
        );
    }
}
