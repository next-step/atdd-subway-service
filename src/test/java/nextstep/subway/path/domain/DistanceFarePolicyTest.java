package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DistanceFarePolicyTest {
    @Test
    @DisplayName("거리에 따른 추가요금 계산")
    void calculate() {
        // given & when & then
        assertAll(
                () -> assertThat(DistanceFarePolicy.calculate(0)).isEqualTo(0),
                () -> assertThat(DistanceFarePolicy.calculate(10)).isEqualTo(0),
                () -> assertThat(DistanceFarePolicy.calculate(15)).isEqualTo(100),
                () -> assertThat(DistanceFarePolicy.calculate(16)).isEqualTo(200),
                () -> assertThat(DistanceFarePolicy.calculate(50)).isEqualTo(800),
                () -> assertThat(DistanceFarePolicy.calculate(58)).isEqualTo(900),
                () -> assertThat(DistanceFarePolicy.calculate(59)).isEqualTo(1000)
        );
    }
}
