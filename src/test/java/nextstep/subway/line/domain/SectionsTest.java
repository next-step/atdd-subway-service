package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SectionsTest {
    private Station upStation;
    private Station downStation;
    private Line line;
    private Section section;
    private Sections sections = new Sections();

    @BeforeEach
    void setUp() {
        upStation = new Station("강남역");
        downStation = new Station("판교역");
        line = Line.builder()
                .name("신분당선")
                .color("red")
                .upStation(upStation)
                .downStation(downStation)
                .distance(5)
                .build();
        section = new Section(line, upStation, downStation, 10);
        sections.add(section);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void add() {
        assertThat(sections.values()).hasSize(1);
    }

    @DisplayName("구간을 삭제한다.")
    @Test
    void remove() {
        Section newSection = new Section(line, new Station("광교역"), downStation, 10);
        sections.add(newSection);
        sections.remove(newSection);

        assertThat(sections.values()).hasSize(1);
    }

    @DisplayName("구간의 모든 역을 조회한다.")
    @Test
    void getStations() {
        assertThat(sections.getStations()).hasSize(2);
    }

    @DisplayName("구간에 지하철역의 존재여부를 반환한다.")
    @Test
    void isStationExisted() {
        assertTrue(sections.isStationExisted(upStation));
        assertTrue(sections.isStationExisted(downStation));
    }

    @DisplayName("구간에 지하철역의 존재여부를 반환한다.")
    @Test
    void isStationNotExisted() {
        assertFalse(sections.isStationNotExisted(upStation));
        assertFalse(sections.isStationNotExisted(downStation));
    }
}
