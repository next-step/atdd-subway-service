package nextstep.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineOfStationInPathsTest {
    private LineWithExtraFee lineWithExtraFee1;
    private LineWithExtraFee lineWithExtraFee2;
    private LineWithExtraFee lineWithExtraFee3;
    private LineWithExtraFee lineWithExtraFee4;
    private LineWithExtraFee lineWithExtraFee5;
    private LineWithExtraFee lineWithExtraFee6;

    @BeforeEach
    void setup() {
        lineWithExtraFee1 = new LineWithExtraFee(1L, BigDecimal.ZERO);
        lineWithExtraFee2 = new LineWithExtraFee(2L, BigDecimal.ONE);
        lineWithExtraFee3 = new LineWithExtraFee(3L, BigDecimal.TEN);
        lineWithExtraFee4 = new LineWithExtraFee(4L, BigDecimal.TEN);
        lineWithExtraFee5 = new LineWithExtraFee(5L, BigDecimal.TEN);
        lineWithExtraFee6 = new LineWithExtraFee(6L, BigDecimal.TEN);
    }

    @DisplayName("환승 가능한 요소들만 골라낼 수 있다.")
    @Test
    void findMultiLinesTest() {
        List<LineOfStationInPath> values = Arrays.asList(
                new LineOfStationInPath(Collections.singletonList(lineWithExtraFee1)),
                new LineOfStationInPath(Collections.singletonList(lineWithExtraFee1)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee1, lineWithExtraFee2)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee2, lineWithExtraFee3))
        );

        List<LineOfStationInPath> expectedValues = Arrays.asList(
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee1, lineWithExtraFee2)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee2, lineWithExtraFee3)));

        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);

        assertThat(lineOfStationInPaths.findMultiLines()).isEqualTo(expectedValues);
    }

    @DisplayName("주어진 요소의 다음 요소를 찾아낼 수 있다.")
    @Test
    void findNextTest() {
        LineOfStationInPath target = new LineOfStationInPath(Arrays.asList(lineWithExtraFee1, lineWithExtraFee2));
        LineOfStationInPath expected = new LineOfStationInPath(Collections.singletonList(lineWithExtraFee2));

        List<LineOfStationInPath> values = Arrays.asList(
                new LineOfStationInPath(Collections.singletonList(lineWithExtraFee1)),
                new LineOfStationInPath(Collections.singletonList(lineWithExtraFee1)),
                target,
                expected
        );

        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);

        assertThat(lineOfStationInPaths.findNext(target)).isEqualTo(expected);
    }

    @DisplayName("마지막 요소의 다음 요소를 탐색 시도 시 빈 껍데기 오브젝트를 반환한다.")
    @Test
    void findNextOfLastTest() {
        LineOfStationInPath target = new LineOfStationInPath(Collections.singletonList(lineWithExtraFee2));
        List<LineOfStationInPath> values = Arrays.asList(
                new LineOfStationInPath(Collections.singletonList(lineWithExtraFee1)),
                new LineOfStationInPath(Collections.singletonList(lineWithExtraFee1)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee1, lineWithExtraFee2)),
                target
        );

        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);

        assertThat(lineOfStationInPaths.findNext(target))
                .isEqualTo(new LineOfStationInPath(new ArrayList<>()));
    }

    @DisplayName("환승한 노선 정보들을 알아낼 수 있다.")
    @Test
    void findTransferLines() {
        List<LineOfStationInPath> values = Arrays.asList(
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee1)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee1)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee1, lineWithExtraFee2, lineWithExtraFee3)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee2)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee2)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee2, lineWithExtraFee3, lineWithExtraFee4)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee3, lineWithExtraFee4, lineWithExtraFee5)),
                new LineOfStationInPath(Arrays.asList(lineWithExtraFee5, lineWithExtraFee6))
        );

        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);

        assertThat(lineOfStationInPaths.findTransferCandidates()).isEqualTo(Arrays.asList(
                new TransferCandidates(Collections.singletonList(lineWithExtraFee2)),
                new TransferCandidates(Arrays.asList(lineWithExtraFee3, lineWithExtraFee4)),
                new TransferCandidates(Collections.singletonList(lineWithExtraFee5))
        ));
    }
}