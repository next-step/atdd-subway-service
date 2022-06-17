package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FeeTest {

    @Test
    @DisplayName("객체가 같은지 검증")
    void verifySameFee() {
        assertThat(Fee.of(10)).isEqualTo(Fee.of(10));
    }

    @Test
    @DisplayName("기본거리(10km 이하)일 때 요금이 맞는지 검증")
    void calculateFeeOfBasicDistance() {
        assertThat(Fee.of(10).getFee()).isEqualTo(1250);
    }

    @ParameterizedTest(name = "{0}km 첫번째 구간(10km 초과 50km 이하)일 때 {1} 요금이 맞는지 검증")
    @CsvSource(value = {"11:1350", "15:1350", "16:1450", "50:2050"}, delimiter = ':')
    void calculateFeeOfFirstSectionDistance(int distance, int expectedFee) {
        assertThat(Fee.of(distance).getFee()).isEqualTo(expectedFee);
    }

    @ParameterizedTest(name = "{0}km 두번째 구간(50km 초과)일 때 {1} 요금이 맞는지 검증")
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void calculateFeeOfSecondSectionDistance(int distance, int expectedFee) {
        assertThat(Fee.of(distance).getFee()).isEqualTo(expectedFee);
    }
}
