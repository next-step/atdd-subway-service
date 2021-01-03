package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineOfStationInPathTest {
    @DisplayName("TransferLineCandidate 컬렉션을 통해 오브젝트를 생성할 수 있다.")
    @Test
    void createTest() {
        List<LineWithExtraFee> lineWithExtraFees = Arrays.asList(
                new LineWithExtraFee(1L, BigDecimal.ZERO),
                new LineWithExtraFee(2L, BigDecimal.TEN)
        );

        LineOfStationInPath lineOfStationInPath = new LineOfStationInPath(lineWithExtraFees);

        assertThat(lineOfStationInPath).isEqualTo(new LineOfStationInPath(lineWithExtraFees));
    }

    @DisplayName("갖고 있는 후보 중 가장 환승 추가금이 적은 Line을 찾아낼 수 있다.")
    @Test
    void findMinOfExtraFeeTest() {
        List<LineWithExtraFee> lineWithExtraFees = Arrays.asList(
                new LineWithExtraFee(1L, BigDecimal.ZERO),
                new LineWithExtraFee(2L, BigDecimal.TEN)
        );

        LineOfStationInPath lineOfStationInPath = new LineOfStationInPath(lineWithExtraFees);

        assertThat(lineOfStationInPath.findMinOfExtraFee()).isEqualTo(new LineWithExtraFee(1L, BigDecimal.ZERO));
    }
}