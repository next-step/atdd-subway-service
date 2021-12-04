package nextstep.subway.line.domain;

import nextstep.subway.common.exception.distance.IllegalDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : DistanceTest
 * author : haedoang
 * date : 2021/12/01
 * description : Distance 객체 테스트
 */
public class DistanceTest {

    @Test
    @DisplayName("유효한 거리 생성하기")
    public void create() throws Exception {
        // given
        Distance distance = Distance.of(5);

        // when
        boolean result = distance.isLessThanOrEqualTo(Distance.of(10));
        boolean result2 = distance.isLessThanOrEqualTo(Distance.of(Distance.MIN_DISTANCE));

        //then
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @ParameterizedTest(name = "유효하지 않은 Distance: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
    public void invalidDistance(int candidate) {
        // then
        assertThatThrownBy(() -> Distance.of(candidate))
                .isInstanceOf(IllegalDistanceException.class)
                .hasMessageContaining("거리는 1보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("Distance 값을 업데이트 하기")
    public void updateDistance() throws Exception {
        //given
        Distance distance5 = Distance.of(5);
        Distance distance10 = Distance.of(10);

        //when
        Distance plusResult = distance5.plus(distance10);
        Distance minusResult = distance10.minus(distance5);

        //then
        assertThat(plusResult).isEqualTo(Distance.of(15));
        assertThat(minusResult).isEqualTo(Distance.of(5));
    }
}
