package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.InvalidLineInPathException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
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

    @DisplayName("길이가 0인 상태로 만들 수 없다.")
    @Test
    void createFailTest() {
        assertThatThrownBy(() -> new LineOfStationInPath(new ArrayList<>()))
                .isInstanceOf(InvalidLineInPathException.class)
                .hasMessage("최소 한개 이상의 노선이 있어야 합니다.");
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