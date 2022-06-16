package nextstep.subway.sections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.sections.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    @Test
    public void getAllStation() {
        //given
        Sections sections = new Sections();
        Section section1 = new Section(new Station("강남역"), new Station("판교역"), 10);
        Section section2 = new Section(new Station("판교역"), new Station("광교역"), 10);
        sections.add(section1);
        sections.add(section2);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("판교역"),
            new Station("광교역"));
    }

    @Test
    public void updateSectionInMiddle() {
        //given
        Sections sections = new Sections();
        Section section1 = new Section(new Station("강남역"), new Station("판교역"), 10);
        Section section2 = new Section(new Station("강남역"), new Station("양재역"), 3);
        sections.add(section1);
        sections.updateSection(section2);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("양재역"),
            new Station("판교역"));
    }
}
