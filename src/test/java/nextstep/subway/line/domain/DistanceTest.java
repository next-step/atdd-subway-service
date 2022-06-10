package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DistanceTest {

    @DisplayName("기존 거리에 거리를 추가하면 해당 결과의 객체가 새로 반환되어야 한다")
    @Test
    void addDistanceThenReturnResultTest() {
        // given
        Distance original = 거리_생성(10);

        // when
        Distance addResult = original.addThenReturnResult(거리_생성(20));

        // then
        assertThat(original).isNotEqualTo(addResult);
        assertThat(addResult.getValue()).isEqualTo(30);
    }

    @DisplayName("기존 거리에 일정 거리를 빼면 해당 거리만큼 줄어들어야 한다")
    @Test
    void subtractDistanceTest() {
        // given
        Distance distance = 거리_생성(10);

        // when
        distance.subtract(거리_생성(5));

        // then
        assertThat(distance.getValue()).isEqualTo(5);
    }

    @DisplayName("기존 거리보다 큰 거리를 빼면 해당 예외가 발생해야 한다")
    @Test
    void subtractBiggestDistanceTest() {
        // given
        Distance distance = 거리_생성(10);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> distance.subtract(거리_생성(11)));
    }

    @DisplayName("작거나 같은 거리를 비교하는 메소드는 정상 동작해야 한다")
    @Test
    void lessOrEqualDistanceCompareTest() {
        // given
        Distance target = 거리_생성(10);
        Distance sameDistance = 거리_생성(10);
        Distance smallerDistance = 거리_생성(9);

        // when
        boolean sameResult = target.isLessThanOrEqualTo(sameDistance);
        boolean smallerResult = target.isLessThanOrEqualTo(smallerDistance);

        // then
        assertThat(sameResult).isTrue();
        assertThat(smallerResult).isFalse();
    }

    public static Distance 거리_생성(int value) {
        return new Distance(value);
    }
}
