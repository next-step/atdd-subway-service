package nextstep.subway.sections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import jdk.nashorn.internal.ir.annotations.Ignore;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.sections.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    @Test
    public void getAllStation() {
        //given
        Sections sections = new Sections(new Section(new Station("강남역"), new Station("판교역"), 10));
        Section section = new Section(new Station("판교역"), new Station("광교역"), 10);
        sections.updateSection(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("판교역"),
            new Station("광교역"));
    }

    @Test
    public void updateSectionInMiddle() {
        //given
        Sections sections = new Sections(new Section(new Station("강남역"), new Station("판교역"), 10));
        Section section = new Section(new Station("강남역"), new Station("양재역"), 3);
        sections.updateSection(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("양재역"),
            new Station("판교역"));
    }

    @Ignore
    @Test
    public void deleteSection() {
        //given
        Sections sections = new Sections(new Section(new Station("강남역"), new Station("판교역"), 10));
        Section section = new Section(new Station("판교"), new Station("양재역"), 3);
        sections.delete(section);
        //when
        List<Station> stations = sections.orderedStations();
        //then
        assertThat(stations).containsExactly(new Station("강남역"), new Station("양재역"),
            new Station("판교역"));
    }
}
