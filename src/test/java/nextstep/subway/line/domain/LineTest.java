package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @Test
    @DisplayName("노선에 구간을 추가한다.")
    void 구간_추가() {
        Line line = new Line("1호선", "dark-blue");
        Section section = new Section(line, new Station("상행종점역"), new Station("하행종점역"), 10);
        assertThat(line.getSections()).hasSize(0);
        line.addSection(section);
        assertThat(line.getSections()).hasSize(1);
        assertThat(line.getSections()).containsExactly(section);
    }
}
