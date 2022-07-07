package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("요금 계산기")
class FareCalculatorTest {

    private FareCalculator fareCalculator = new FareCalculator();

    @DisplayName("10Km 이내 기본운임을 계산할 수 있다.")
    @Test
    void 기본운임_계산() {
        assertThat(fareCalculator.calculate(Distance.from(9))).isEqualTo(1250);
    }

    @DisplayName("이용 거리 10Km 초과 시 5Km마다 100원씩 추가운임 부과할 수 있다.")
    @Test
    void 이용거리_초과_10KM_50KM까지의_계산() {
        assertThat(fareCalculator.calculate(Distance.from(11))).isEqualTo(1350);
    }

    @DisplayName("이용 거리 50Km 초과 시 8Km마다 100원씩 추가운임 부과할 수 있다.")
    @Test
    void 이용거리_50KM_초과_계산() {
        assertThat(fareCalculator.calculate(Distance.from(57))).isEqualTo(2150);
    }
}
