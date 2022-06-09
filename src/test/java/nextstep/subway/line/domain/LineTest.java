package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LineTest {

    @DisplayName("Line 은 Section 을 추가 할수 있다.")
    @Test
    void createLineTest() {
        final Line 신분당선 = new Line("신분당선", "bg-blue-200");
        Station 수원역 = new Station("수원역");
        Station 병점역 = new Station("병점역");
        Section section = new Section(신분당선, 수원역, 병점역, 10);
        신분당선.addSection(section);
        assertThat(신분당선).isEqualTo(new Line("신분당선", "bg-blue-200", 수원역, 병점역, 10));
    }

}