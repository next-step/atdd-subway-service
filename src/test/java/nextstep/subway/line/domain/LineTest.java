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

    @Test
    @DisplayName("노선에 역을 삭제한다.")
    void 역_삭제() {
        Line line = new Line("1호선", "dark-blue");
        Station 신도림역 = new Station("신도림역");
        Section 구로_신도림 = new Section(line, new Station("구로역"), 신도림역, 10);
        Section 신도림_영등포 = new Section(line, 신도림역, new Station("영등포역"), 10);
        Section 영등포_신길 = new Section(line, new Station("영등포역"), new Station("신길역"), 10);

        line.addSection(구로_신도림);
        line.addSection(신도림_영등포);
        line.addSection(영등포_신길);

        assertThat(line.getSections()).hasSize(3);
        line.removeLineStation(신도림역);
        assertThat(line.getSections()).hasSize(2);
    }
}
