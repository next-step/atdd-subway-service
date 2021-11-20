package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("구간 거리 테스트")
class DistanceTest {

    @Test
    @DisplayName("구간을 합친다.")
    void getMergedDistance() {
        // given
        Distance distance = new Distance(10);

        // when
        Distance mergedDistance = distance.getAddedDistance(new Distance(15));

        // then
        assertThat(mergedDistance).isEqualTo(new Distance(25));
    }

    @Test
    @DisplayName("구간을 분리한다.")
    void getSplitDistance() {
        // given
        Distance distance = new Distance(10);

        // when
        Distance splitDistance = distance.getRemainedDistance(new Distance(6));

        // then
        assertThat(splitDistance).isEqualTo(new Distance(4));
    }

    @Test
    @DisplayName("기존 거리보드 먼 거리로 구간을 분리하면 예외가 발생한다.")
    void getSplitDistanceThrowException() {
        // given
        Distance distance = new Distance(10);

        // when & then
        assertThatExceptionOfType(DistanceSplitFaildException.class)
                .isThrownBy(() -> distance.getRemainedDistance(new Distance(15)))
                .withMessageMatching("역과 역 사이의 거리보다 좁은 거리를 입력해주세요.");
    }
}
