package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("노선 목록을 관리하는 클래스 테스트")
class LinesTest {

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "bg-green-600", 100);
        삼호선 = new Line("3호선", "bg-orange-600", 500);
        신분당선 = new Line("신분당선", "bg-red-600", 1000);
    }

    @Test
    void 호선의_운임_비용_중_가장_비싼_비용을_구함() {
        Lines lines = new Lines(Arrays.asList(삼호선, 신분당선, 이호선));

        assertEquals(1000, lines.findMaxFare());
    }
}
