package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    private final Line 일호선 = new Line("1호선", "dark-blue");
    private final Station 구로역 = new Station("구로역");
    private final Station 신도림역 = new Station("신도림역");
    private final Station 영등포역 = new Station("영등포역");
    private final Station 신길역 = new Station("신길역");
    private final Distance 거리_10 = new Distance(10);

    @Test
    @DisplayName("구간을 추가한다.")
    void 구간_추가() {
        Sections sections = new Sections();
        sections.addSection(일호선, 구로역, 신도림역, 거리_10);
        sections.addSection(일호선, 신도림역, 영등포역, 거리_10);
        sections.addSection(일호선, 영등포역, 신길역, 거리_10);

        assertThat(sections.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("구간에 포함된 역을 조회한다.")
    void 구간_포함_역_조회() {
        Sections sections = new Sections();
        sections.addSection(일호선, 구로역, 신도림역, 거리_10);
        sections.addSection(일호선, 신도림역, 영등포역, 거리_10);
        sections.addSection(일호선, 영등포역, 신길역, 거리_10);

        assertThat(sections.getStations()).containsOnly(구로역, 신도림역, 영등포역, 신길역);
    }

    @Test
    @DisplayName("구간에 포함된 역을 삭제한다.")
    void 구간_포함_역_삭제() {
        Sections sections = new Sections();
        sections.addSection(일호선, 구로역, 신도림역, 거리_10);
        sections.addSection(일호선, 신도림역, 영등포역, 거리_10);
        sections.addSection(일호선, 영등포역, 신길역, 거리_10);

        assertThat(sections.getStations()).containsOnly(구로역, 신도림역, 영등포역, 신길역);
        sections.removeLineStation(일호선, 신도림역);
        assertThat(sections.getStations()).containsOnly(구로역, 영등포역, 신길역);
    }

    @Test
    @DisplayName("구간에 포함된 노선을 조회한다.")
    void 구간_포함_노선_조회() {
        Line 이호선 = new Line("2호선", "green");
        Line 칠호선 = new Line("7호선", "dark-green");
        Station 대림역 = new Station("대림역");
        Station 남구로역 = new Station("남구로역");
        Sections sections = new Sections();
        sections.addSection(일호선, 구로역, 신도림역, 거리_10);
        sections.addSection(이호선, 신도림역, 대림역, 거리_10);
        sections.addSection(칠호선, 대림역, 남구로역, 거리_10);

        assertThat(sections.findLinesContainedStations(Arrays.asList(구로역, 신도림역, 대림역, 남구로역))).containsOnly(일호선, 이호선, 칠호선);
    }
}
