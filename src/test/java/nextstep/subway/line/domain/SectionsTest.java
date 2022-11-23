package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    private final Line line = new Line("1호선", "dark-blue");
    private final Station 구로역 = new Station("구로역");
    private final Station 신도림역 = new Station("신도림역");
    private final Station 영등포역 = new Station("영등포역");
    private final Station 신길역 = new Station("신길역");

    @Test
    @DisplayName("구간을 추가한다.")
    void 구간_추가() {
        Sections sections = new Sections();
        sections.addSection(line, 구로역, 신도림역, 10);
        sections.addSection(line, 신도림역, 영등포역, 10);
        sections.addSection(line, 영등포역, 신길역, 10);

        assertThat(sections.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("구간에 포함된 역을 조회한다.")
    void 구간_포함_역_조회() {
        Sections sections = new Sections();
        sections.addSection(line, 구로역, 신도림역, 10);
        sections.addSection(line, 신도림역, 영등포역, 10);
        sections.addSection(line, 영등포역, 신길역, 10);

        assertThat(sections.getStations()).containsOnly(구로역, 신도림역, 영등포역, 신길역);
    }

    @Test
    @DisplayName("구간에 포함된 역을 삭제한다.")
    void 구간_포함_역_삭제() {
        Sections sections = new Sections();
        sections.addSection(line, 구로역, 신도림역, 10);
        sections.addSection(line, 신도림역, 영등포역, 10);
        sections.addSection(line, 영등포역, 신길역, 10);

        assertThat(sections.getStations()).containsOnly(구로역, 신도림역, 영등포역, 신길역);
        sections.removeLineStation(line, 신도림역);
        assertThat(sections.getStations()).containsOnly(구로역, 영등포역, 신길역);
    }
}
