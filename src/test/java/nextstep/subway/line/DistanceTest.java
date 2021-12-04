package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import nextstep.subway.*;
import nextstep.subway.line.domain.*;

class DistanceTest extends AcceptanceTest {
    @DisplayName("디폴트 생성자를 호출하면 Distance 객체를 반환한다.")
    @Test
    void constructTest() {
        assertThat(new Distance()).isInstanceOf(Distance.class);
    }

    @DisplayName("정적 팩토리 메서드 from으로 거리를 입력받으면 Distance 객체를 반환한다.")
    @Test
    void staticFactoryMethodTest() {
        assertThat(Distance.from(1)).isInstanceOf(Distance.class);
    }

    @DisplayName("정적 팩토리 메서드 from으로 움수를 입력받으면 예외를 던진다.")
    @Test
    void exceptionTest() {
        assertThatThrownBy(() -> Distance.from(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("add함수로 Distance 객체를 입력받으면, 호출객체의 거리와 인자의 객체의 거리를 더한 Distance 객체를 던진다.")
    @Test
    void addTest() {
        assertThat(Distance.from(1).add(Distance.from(2))).isEqualTo(Distance.from(3));
    }

    @DisplayName("subtract함수로 Distance 객체를 입력받으면, 호출객체의 거리에서 인자의 객체의 거리를 뺀 Distance 객체를 던진다.")
    @Test
    void subtractTest() {
        assertThat(Distance.from(3).subtract(Distance.from(2))).isEqualTo(Distance.from(1));
    }
}
