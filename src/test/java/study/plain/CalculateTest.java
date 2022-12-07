package study.plain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateTest {
    @Test
    void name() {
        int overDistance = -6;
        int fareUnitDistance = 5;
        if (overDistance >= 40) {
            fareUnitDistance = 8;
        }
        int farePerUnitDistance = 100;

        int fare = (int) ((Math.ceil((overDistance - 1) / fareUnitDistance) + 1) * farePerUnitDistance);
        assertThat(fare).isEqualTo(0);
    }
}
