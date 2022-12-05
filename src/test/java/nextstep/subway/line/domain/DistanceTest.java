package nextstep.subway.line.domain;

import nextstep.subway.line.message.DistanceMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    /**
     * Given 생성자에 숫자가 주어진 경우
     * When 거리 생성시
     * Then 정상적으로 생성된다
     */
    @DisplayName("거리 생성 - 생성자에 숫자가 주어진 경우")
    @Test
    void create_distance_test() {
        // given
        int distanceValue = 10;
        
        // when
        Distance distance = new Distance(distanceValue);

        // then
        assertThat(distance).isEqualTo(new Distance(distanceValue));
    }
    
    /**
     * Given 생성자에 1미만 숫자가 주어진 경우
     * When 거리 생성시
     * Then 예외 처리한다
     */
    @DisplayName("거리 생성 예외 처리 - 생성자에 1미만 숫자가 주어진 경우")
    @Test
    void create_distance_with_invalid_number_test() {
        // given
        int distanceValue = 0;
        
        // when & then
        assertThatThrownBy(() -> new Distance(distanceValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DistanceMessage.CREATE_ERROR_MORE_THAN_ONE.message());
    }
    
    /**
     * Given 합산해야되는 거리가 주어진 경우
     * When 거리 합산시
     * Then 정상적으로 합산된다
     */
    @DisplayName("두 거리의 합을 계산")
    @Test
    void plus_distance_test() {
        // given
        Distance distance = new Distance(10);
        Distance other = new Distance(20);

        // when
        Distance calculatedDistance = distance.plus(other);

        // then
        assertThat(calculatedDistance).isEqualTo(new Distance(30));
    }
    
    /**
     * Given 감소해야되는 거리가 주어진 경우
     * When 거리 감소시
     * Then 정상적으로 감소한다
     */
    @DisplayName("두 거리의 차를 계산")
    @Test
    void minus_distance_test() {
        // given
        Distance distance = new Distance(20);
        Distance other = new Distance(10);

        // when
        Distance calculatedDistance = distance.minus(other);

        // then
        assertThat(calculatedDistance).isEqualTo(new Distance(10));
    }
}
