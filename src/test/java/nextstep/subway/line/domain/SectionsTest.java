package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.line.domain.SectionsTestFixture.createSections;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @DisplayName("구역을 정렬된 형태로 조회하면 상행역부터 하행역까지 정렬되어 조회된다.")
    @Test
    void findInOrderStations() {
        //given
        Station 양재역 = createStation("양재역");
        Station 양재시민의숲역 = createStation("양재시민의숲역");
        Station 청계산입구역 = createStation("청계산입구역");
        Line line = createLine("신분당선", "bg-red", createStation("강남역"), 양재역, 10);
        Section section1 = createSection(line, 양재역, 양재시민의숲역, 15);
        Section section2 = createSection(line, 양재시민의숲역, 청계산입구역, 10);

        //when
        Sections sections = createSections(Arrays.asList(section1, section2));
        List<Station> stations = sections.findInOrderStations();

        //then
        assertThat(stations).containsExactly(양재역, 양재시민의숲역, 청계산입구역);
    }
}
