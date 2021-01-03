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
}