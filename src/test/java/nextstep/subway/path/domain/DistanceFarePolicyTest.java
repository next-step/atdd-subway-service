package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    @DisplayName("거리별 요금 계산을 한다.")
    @ParameterizedTest
    @CsvSource({
        "5, 1250",
        "10, 1250",
        "11, 1350",
        "51, 2150",
    })
    void test(int distance, int expected) {
        int actual = DistanceFarePolicy.calculate(distance);
        assertThat(actual).isEqualTo(expected);
    }
}
