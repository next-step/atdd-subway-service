package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("지하철역이 상행역에서 하행역 순으로 출력되어야 한다.")
    void get_stations_should_ordered_by_up_to_down() {
        Sections sections = new Sections();
        sections.addSection(createSection("1번역", "3번역", 100));
        sections.addSection(createSection("3번역", "5번역", 50));

        // when
        sections.addSection(createSection("5번역", "8번역", 60));

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(createStations("1번역", "3번역", "5번역", "8번역"));
    }

    private Section createSection(String upStationName, String downStationName, int distance) {
        return new Section(new Line(), new Station(upStationName), new Station(downStationName), distance);
    }

    private List<Station> createStations(String... names) {
        return Arrays.stream(names)
                .map(this::createStation).
                collect(Collectors.toList());
    }

    private Station createStation(String name) {
        return new Station(name);
    }
}
