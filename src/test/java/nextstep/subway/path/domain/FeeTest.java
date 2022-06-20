package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineTest.라인_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import nextstep.subway.line.domain.Line;
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

    @Test
    @DisplayName("추가요금을 가지고 있는 노선이 포함된 경우 추가요금이 포함되는지 검증")
    void calculateFeeOfLineExtraCharge() {
        Line 신분당선 = 라인_생성("신분당선", "빨간색", 1000);
        Line 이호선 = 라인_생성("이호선", "초록색", 0);

        assertThat(Fee.of(10, new HashSet<>(Arrays.asList(신분당선, 이호선))).getFee()).isEqualTo(2250);
    }

    @Test
    @DisplayName("추가요금을 가지고 있는 노선이 포함된 가장 큰 추가요금이 포함되는지 검증")
    void calculateFeeOfMaxLineExtraCharge() {
        Line 신분당선 = 라인_생성("신분당선", "빨간색", 1000);
        Line 이호선 = 라인_생성("이호선", "초록색", 500);

        assertThat(Fee.of(10, new HashSet<>(Arrays.asList(신분당선, 이호선))).getFee()).isEqualTo(2250);
    }
}
