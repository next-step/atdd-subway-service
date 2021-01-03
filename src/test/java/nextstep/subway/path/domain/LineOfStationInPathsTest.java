package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineOfStationInPathsTest {
//    @DisplayName("환승 가능한 요소들만 골라낼 수 있다.")
//    @Test
//    void findMultiLinesTest() {
//        List<LineOfStationInPath> values = Arrays.asList(
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L, 2L)),
//                new LineOfStationInPath(Arrays.asList(2L, 3L))
//        );
//
//        List<LineOfStationInPath> expectedValues = Arrays.asList(
//                new LineOfStationInPath(Arrays.asList(1L, 2L)),
//                new LineOfStationInPath(Arrays.asList(2L, 3L)));
//
//        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);
//
//        assertThat(lineOfStationInPaths.findMultiLines()).isEqualTo(expectedValues);
//    }
//
//    @DisplayName("주어진 요소의 다음 요소를 찾아낼 수 있다.")
//    @Test
//    void findNextTest() {
//        List<LineOfStationInPath> values = Arrays.asList(
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L, 2L)),
//                new LineOfStationInPath(Arrays.asList(2L))
//        );
//
//        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);
//
//        assertThat(lineOfStationInPaths.findNext(new LineOfStationInPath(Arrays.asList(1L, 2L))))
//                .isEqualTo(new LineOfStationInPath(Arrays.asList(2L)));
//    }
//
//    @DisplayName("마지막 요소의 다음 요소를 탐색 시도 시 빈 껍데기 오브젝트를 반환한다.")
//    @Test
//    void findNextOfLastTest() {
//        List<LineOfStationInPath> values = Arrays.asList(
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L, 2L)),
//                new LineOfStationInPath(Arrays.asList(2L))
//        );
//
//        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);
//
//        assertThat(lineOfStationInPaths.findNext(new LineOfStationInPath(Arrays.asList(2L))))
//                .isEqualTo(new LineOfStationInPath(new ArrayList<>()));
//    }
//
//    @DisplayName("환승한 노선 정보들을 알아낼 수 있다.")
//    @Test
//    void findTransferLines() {
//        List<LineOfStationInPath> values = Arrays.asList(
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L)),
//                new LineOfStationInPath(Arrays.asList(1L, 2L, 3L)),
//                new LineOfStationInPath(Arrays.asList(2L)),
//                new LineOfStationInPath(Arrays.asList(2L)),
//                new LineOfStationInPath(Arrays.asList(2L, 3L, 4L)),
//                new LineOfStationInPath(Arrays.asList(3L, 4L, 5L)),
//                new LineOfStationInPath(Arrays.asList(5L, 6L))
//        );
//
//        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);
//
//        assertThat(lineOfStationInPaths.findTransferLineCandidates()).isEqualTo(Arrays.asList(
//                Arrays.asList(2L), Arrays.asList(3L, 4L), Arrays.asList(5L), new ArrayList<>()
//        ));
//    }
}