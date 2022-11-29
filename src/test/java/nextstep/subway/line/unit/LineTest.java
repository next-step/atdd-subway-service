package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    private Station 강남역 = new Station("강남역");
    private Station 광교역 = new Station("광교역");

    @DisplayName("노선에 구간을 추가할 수 있다.")
    @Test
    void addSection_test() {
        // given
        Line 신분당선 = Line.ofNameAndColor("신분당선", "빨간색");
        Section 구간 = Section.of(신분당선, 강남역, 광교역, 10);
        // when
        신분당선.addSection(구간);

        // then
        assertTrue(신분당선.containSection(구간));
    }

}
