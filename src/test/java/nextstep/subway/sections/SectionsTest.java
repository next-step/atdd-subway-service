package nextstep.subway.sections;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    @DisplayName("정렬된 지하철 역 정보를 반환할 수 있다")
    @Test
    void sortStationsTest() {
        // given
        Station station1 = new Station("판교역");
        Station station2 = new Station("양재역");
        Station station3 = new Station("강남역");

        Section section1 = new Section(station1, station2, 10);
        Section section2 = new Section(station1, station3, 5);

        Sections sections = new Sections(Arrays.asList(section1, section2));

        // when
        List<String> stations = sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        // then
        assertThat(stations).containsExactly("판교역", "강남역", "양재역");
    }
}
