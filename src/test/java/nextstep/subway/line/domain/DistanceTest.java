package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTest {

    @ParameterizedTest(name = "길이 생성 시, 길이가 0보다 작거나 같으면 에러가 발생한다.(distance: {0})")
    @ValueSource(ints = {0, -1, -5})
    void createDistanceThrowErrorWhenDistanceLessThenOrEqualToZero(int distance) {
        //when & then
        assertThatThrownBy(() -> Distance.from(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
    }

    @DisplayName("길이를 생성하면 조회할 수 있다.")
    @Test
    void createDistance() {
        //given
        int actual = 10;

        //when
        Distance distance = Distance.from(actual);
        //then
        assertThat(distance.value()).isEqualTo(actual);
    }

    @DisplayName("길이에서 길이를 빼면 새로운 길이가 나온다.")
    @Test
    void subtractDistance() {
        //given
        int original = 15;
        int subtract = 5;
        Distance distance = Distance.from(original);
        Distance subtractDistance = Distance.from(subtract);

        //when
        Distance resultDistance = distance.subtract(subtractDistance);

        //then
        assertThat(resultDistance.value()).isEqualTo(original - subtract);
    }

    @DisplayName("길이에서 길이를 더하면 새로운 길이가 나온다.")
    @Test
    void addDistance() {
        //given
        int original = 15;
        int add = 5;
        Distance distance = Distance.from(original);
        Distance addDistance = Distance.from(add);

        //when
        Distance resultDistance = distance.add(addDistance);

        //then
        assertThat(resultDistance.value()).isEqualTo(original + add);
    }
}
