package nextstep.subway.line.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LineTest {

    @DisplayName("이름, 색깔 정보로 Line 을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"신분당선,RED", "1호선,BLUE", "2호선,GREEN"})
    void create1(String name, String color) {
        // when
        Line line = Line.of(name, color);

        // then
        assertAll(
            () -> assertEquals(line.getName(), LineName.from(name)),
            () -> assertEquals(line.getColor(), LineColor.from(color))
        );
    }
}