package nextstep.subway.path.domain.fee.distanceFee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SuperLongDistanceFeeTest {
    @DisplayName("범위 내의 거리(50km 초과)로 오브젝트를 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = { 51, 100 })
    void createTest(Integer distance) {
        DistanceFee distanceFee = new SuperLongDistanceFee(distance);

        assertThat(distanceFee).isNotNull();
    }

    @DisplayName("범위 이하의 거리로 오브젝트를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { 50, 49 })
    void createFailTest(Integer tooShortDistance) {
        assertThatThrownBy(() -> new SuperLongDistanceFee(tooShortDistance))
                .isInstanceOf(InvalidFeeDistanceException.class);
    }

    @DisplayName("거리에 따른 추가 요금을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = { "51:2150", "58:2150", "59:2150", "60:2250" }, delimiter = ':')
    void calculateTest(Integer distance, Integer expectedFee) {
        DistanceFee distanceFee = new SuperLongDistanceFee(distance);

        assertThat(distanceFee.calculate()).isEqualTo(BigDecimal.valueOf(expectedFee));
    }
}