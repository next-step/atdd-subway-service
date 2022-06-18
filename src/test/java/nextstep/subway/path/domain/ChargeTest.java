package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ChargeTest {
    @DisplayName("기본요금")
    @Test
    void baseCharge() {
        Charge actual = BaseCharge.BASE_CHARGE;
        Charge expected = new Charge(1250);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("거리별 추가요금")
    @ParameterizedTest
    @CsvSource(value = {"5,0", "25,300", "75,1100"})
    void distanceSurcharge(int distance, int surcharge) {
        Charge actual = DistanceSurcharge.from(distance);
        Charge expected = new Charge(surcharge);
        assertThat(actual).isEqualTo(expected);
    }
}
