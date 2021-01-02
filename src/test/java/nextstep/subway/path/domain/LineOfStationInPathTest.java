package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineOfStationInPathTest {
    @DisplayName("노선 ID 컬렉션을 통해 오브젝트를 생성할 수 있다.")
    @Test
    void createTest() {
        List<Long> lineIds = Arrays.asList(1L, 2L);

        LineOfStationInPath lineOfStationInPath = new LineOfStationInPath(lineIds);

        assertThat(lineOfStationInPath).isEqualTo(new LineOfStationInPath(lineIds));
    }

    @DisplayName("서로 다른 노선의 교차점인지 확인할 수 있다.")
    @ParameterizedTest
    @MethodSource("isMultiLineTestResource")
    void isMultiLineTest(List<Long> lineIds, boolean expected) {
        LineOfStationInPath lineOfStationInPath = new LineOfStationInPath(lineIds);

        assertThat(lineOfStationInPath.isMultiLine()).isEqualTo(expected);
    }
    public static Stream<Arguments> isMultiLineTestResource() {
        return Stream.of(
                Arguments.of(Collections.singletonList(1L), false),
                Arguments.of(Arrays.asList(1L, 2L), true)
        );
    }
}