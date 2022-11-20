package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.line.domain.SectionsTestFixture.createSections;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    @DisplayName("구역을 추가한다.")
    @Test
    void addSection() {
        //given
        Station 양재역 = createStation("양재역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section = createSection(line, 양재역, createStation("양재시민의숲역"), 15);
        Sections sections = createSections(new ArrayList<>());

        //when
        sections.addSection(section);

        // then
        assertThat(sections.findStations()).hasSize(2);
    }


}
