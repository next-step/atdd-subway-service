package nextstep.subway.path.domain.fee.distanceFee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LongDistanceFeeTest {
    @DisplayName("10km가 넘는 거리요금을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = { "11:1350", "15:1350", "16:1450", "20:1450", "21:1550", "25:1550",
            "26:1650", "31:1750", "36:1850", "41:1950", "46:2050", "50:2050" }, delimiter = ':')
    void canCalculateTest(Integer distance, Integer expectedFee) {
        DistanceFee distanceFee = new LongDistanceFee(distance);

        assertThat(distanceFee.calculate()).isEqualTo(BigDecimal.valueOf(expectedFee));
    }

    @DisplayName("범위 내의 값으로 오브젝트를 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = { 11, 50 })
    void createTest(Integer distance) {
        DistanceFee distanceFee = new LongDistanceFee(distance);

        assertThat(distanceFee).isNotNull();
    }

    @DisplayName("범위를 벗어난 오브젝트를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { 10, 51 })
    void createFailTest(Integer invalidDistance) {
        assertThatThrownBy(() -> new LongDistanceFee(invalidDistance)).isInstanceOf(InvalidFeeDistanceException.class);
    }
}