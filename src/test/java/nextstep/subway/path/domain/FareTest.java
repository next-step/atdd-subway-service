package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {


    @DisplayName("거리에 따른 요금을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"0:1250", "10:1250", "15:1350", "20:1450", "40:1850", "50:2050", "58:2150", "122:2950"},
            delimiter = ':')
    void distance_fare(int distance, int expect) {
        Fare actual = Fare.of(distance, Lines.From(new ArrayList<>()), i->i);

        assertThat(actual.getFare()).isEqualTo(expect);
    }

    @DisplayName("노선별 추가 요금을 계산할 수 있다.")
    @ParameterizedTest
    @MethodSource("lineList")
    void line_fare(Line line, int expect) {
        //when
        Fare actual = Fare.of(0, Lines.From(Collections.singletonList(line)), i->i);
        //then
        assertThat(actual.getFare()).isEqualTo(expect);
    }

    private static Stream<Arguments> lineList() {
        return Stream.of(
                Arguments.of(new Line("일호선", "blue", null, null, 0, 100), 1350),
                Arguments.of(new Line("이호선", "green", null, null, 0, 200), 1450),
                Arguments.of(new Line("삼호선", "orange", null, null, 0, 300), 1550),
                Arguments.of(new Line("사호선", "skyblue", null, null, 0, 400), 1650),
                Arguments.of(new Line("오호선", "pink", null, null, 0, 500), 1750));
    }

}
