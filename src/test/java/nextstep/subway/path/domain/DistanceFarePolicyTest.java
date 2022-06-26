package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"10:1250", "15:1350", "48:2050", "56: 2150"}, delimiter = ':')
    void 거리를_기준으로_지하철_요금을_계산한다(int distance, int fare) {
        // given
        DistanceFarePolicy distanceFarePolicy = new DistanceFarePolicy(() -> 0, new Distance(distance));

        // when
        int result = distanceFarePolicy.fare();

        // then
        assertThat(result).isEqualTo(fare);
    }
}
