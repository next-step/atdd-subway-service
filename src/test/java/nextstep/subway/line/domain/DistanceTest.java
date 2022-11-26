package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DistanceTest {

    @Test
    @DisplayName("거리 0 이하 입력 오류")
    void min_distance_error() {
        // then
        assertThatThrownBy(() -> new Distance(-1))
                .isInstanceOf(LineException.class);
    }

    @Test
    @DisplayName("기존 거리보다 많은 거리 마이너스 오류")
    void moreMinusDistance() {
        // given
        Distance distance = new Distance(10);
        // then
        assertThatThrownBy(() -> distance.minusDistance(12))
                .isInstanceOf(LineException.class);
    }

    @Test
    @DisplayName("거리 마이너스")
    void minusDistance() {
        // given
        Distance distance = new Distance(10);
        // when
        distance.minusDistance(8);
        // then
        assertThat(distance.getValue()).isEqualTo(2);
    }

    @Test
    @DisplayName("거리 플러스")
    void plusDistance() {
        // given
        Distance distance = new Distance(10);
        // when
        distance.plusDistance(3);
        // then
        assertThat(distance.getValue()).isEqualTo(13);
    }
}
