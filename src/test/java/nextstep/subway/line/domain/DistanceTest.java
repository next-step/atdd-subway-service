package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("기존의 길이보다 작으면 길이 수정에 성공한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void modify_distance(int input) {
        Distance distance = Distance.from(10);
        int expected = distance.getDistance() - input;

        distance.modifyDistance(Distance.from(input));

        assertThat(distance.getDistance()).isEqualTo(expected);
    }

    @DisplayName("기존의 길이보다 크거나 같으면 길이 수정에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 20})
    void modify_distance_big_or_same(int input) {
        Distance distance = Distance.from(10);
        assertThatThrownBy(() -> distance.modifyDistance(Distance.from(input)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
