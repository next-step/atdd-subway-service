package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineTest.라인_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineExtraFeeHandlerTest {
    private Fee fee;
    private FeeHandler feeHandler;

    @BeforeEach
    void setUp() {
        final List<Line> lines = Arrays.asList(라인_생성("이호선", "초록색", 500), 라인_생성("칠호선", "국방색", 0));
        fee = new Fee();
        feeHandler = new LineExtraFeeHandler(null, new HashSet<>(lines));
    }

    @Test
    @DisplayName("노선의 추가비용이 잘 반영되는지 확인")
    void addLineMaxExtraCharge() {
        feeHandler.calculate(fee);

        assertThat(fee.getFee()).isEqualTo(1750);
    }
}
