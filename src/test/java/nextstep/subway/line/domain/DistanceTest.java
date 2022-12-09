package nextstep.subway.line.domain;

import nextstep.subway.common.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DistanceTest {

    @DisplayName("생성자로 거리값을 생성한다.")
    @Test
    void 생성자로_거리_생성_테스트() {
        Distance distance = new Distance(10);

        assertAll(
                () -> assertThat(distance).isNotNull(),
                () -> assertThat(distance.value()).isEqualTo(10)
        );
    }

    @DisplayName("유효하지 않은 값으로 거리를 생성할 때 예외가 발생한다.")
    @Test
    void 유효하지_않은_거리_생성_테스트() {

        assertThatThrownBy(
                () -> new Distance(-10)
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("두 거리값의 합을 계산하여 값을 치환한다.")
    @Test
    void 거리_값_합_계산_테스트() {
        Distance previousDistance = new Distance(20);
        Distance newDistance = new Distance(5);

        previousDistance.add(newDistance);

        assertThat(previousDistance.value()).isEqualTo(25);
    }

    @DisplayName("두 거리값의 차를 계산하여 값을 치환한다.")
    @Test
    void 거리_값_차이_계산_테스트() {
        Distance previousDistance = new Distance(10);
        Distance newDistance = new Distance(4);

        previousDistance.subtract(newDistance);

        assertThat(previousDistance.value()).isEqualTo(6);

    }

    @DisplayName("기존 거리가 신규 거리보다 작은 경우 예외가 발생한다.")
    @Test
    void 두_거리_유효성_검증_테스트() {
        Distance previousDistance = new Distance(10);
        Distance newDistance = new Distance(12);

        assertThatThrownBy(
                () -> previousDistance.validateDistance(newDistance)
        ).isInstanceOf(InvalidDataException.class);
    }
}
