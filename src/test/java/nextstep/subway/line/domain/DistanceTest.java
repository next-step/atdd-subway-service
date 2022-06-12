package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistanceTest {
    @Test
    @DisplayName("Distance 생성")
    void Distance_생성(){
        Distance distance = Distance.from(10);
        assertThat(distance.value()).isEqualTo(10);
    }

    @Test
    @DisplayName("Distance 생성 실패")
    void Distance_생성_실패(){
        assertThrows(IllegalArgumentException.class, () -> Distance.from(0));
    }

    @Test
    @DisplayName("Distance 더하기")
    void Distance_더하기(){
        Distance distance = Distance.from(10);
        distance.add(Distance.from(10));
        assertThat(distance.value()).isEqualTo(20);
    }

    @Test
    @DisplayName("Distance 빼기")
    void Distance_빼기(){
        Distance distance = Distance.from(10);
        distance.subtract(Distance.from(5));
        assertThat(distance.value()).isEqualTo(5);
    }
}
