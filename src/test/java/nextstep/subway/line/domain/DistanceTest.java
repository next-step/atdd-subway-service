package nextstep.subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 길이 관련 기능")
public class DistanceTest {
    private Distance 구간거리100;
    private Distance 구간거리60;
    private Distance 구간거리40;
    
    @BeforeEach
    public void setUp() {
        // given
        구간거리100 = Distance.of(100);
        구간거리60 = Distance.of(60);
        구간거리40 = Distance.of(40);
    }
    
    @DisplayName("기등록된 거리에 다른 거리를 합한다.")
    @Test
    void plus_otherDistance() {
        // when
        Distance plusedDistance = 구간거리60.plus(구간거리40);

        // then
        Assertions.assertThat(plusedDistance).isEqualTo(구간거리100);
    }
    
    @DisplayName("기등록된 거리에 다른 거리를 뺀다.")
    @Test
    void minus_otherDistance() {
        // when
        Distance minusedDistance = 구간거리100.minus(구간거리40);

        // then
        Assertions.assertThat(minusedDistance).isEqualTo(구간거리60);
    }

    
    @DisplayName("기등록된 거리보다 큰거리를 뺼 시 에러가 발생한다.")
    @Test
    void exception_minus_greaterOtherDistance() {
        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> 구간거리40.minus(구간거리100));
    }
}
