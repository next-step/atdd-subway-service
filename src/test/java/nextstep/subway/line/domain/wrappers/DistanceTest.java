package nextstep.subway.line.domain.wrappers;

import nextstep.subway.exception.BadDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("구간 거리 객체 테스트")
public class DistanceTest {

    @Test
    void 구간_거리_객체_생성() {
        Distance distance = new Distance(7);
        assertThat(distance).isEqualTo(new Distance(7));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 구간_거리_값이_1보다_작은_경우_에러발생(int distance) {
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(BadDistanceException.class)
                .hasMessage("구간 거리 값은 0보다 큰값만 입력 가능 합니다.");
    }

    @Test
    void 두개의_구간거리_빼기() {
        Distance distance = new Distance(7);
        Distance other = new Distance(3);
        assertThat(distance.subtractDistance(other)).isEqualTo(new Distance(4));
    }

    @Test
    void 두개의_구간_거리_빼기_결과가_0_또는_음수가_발생하는_경우_에러_발생() {
        Distance distance = new Distance(3);
        Distance other = new Distance(7);
        assertThatThrownBy(() -> distance.subtractDistance(other)).isInstanceOf(BadDistanceException.class)
                .hasMessage("구간 거리 값은 0보다 큰값만 입력 가능 합니다.");
    }

    @Test
    void 두개의_구간_거리_더하기() {
        Distance distance = new Distance(7);
        Distance other = new Distance(3);
        assertThat(distance.sumDistance(other)).isEqualTo(new Distance(10));
    }
}
