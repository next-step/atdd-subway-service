package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineOfStationInPathsTest {
    @DisplayName("환승 가능한 요소들만 골라낼 수 있다.")
    @Test
    void findMultiLinesTest() {
        List<LineOfStationInPath> values = Arrays.asList(
                new LineOfStationInPath(Arrays.asList(1L)),
                new LineOfStationInPath(Arrays.asList(1L)),
                new LineOfStationInPath(Arrays.asList(1L, 2L)),
                new LineOfStationInPath(Arrays.asList(2L, 3L)
        ));

        List<LineOfStationInPath> expectedValues = Arrays.asList(
                new LineOfStationInPath(Arrays.asList(1L, 2L)),
                new LineOfStationInPath(Arrays.asList(2L, 3L)));

        LineOfStationInPaths lineOfStationInPaths = new LineOfStationInPaths(values);

        assertThat(lineOfStationInPaths.findMultiLines()).isEqualTo(expectedValues);
    }
}