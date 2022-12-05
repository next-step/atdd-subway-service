package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("기존 구간의 하행역을 포함하여 새로운 구간이 들어오게 되면, 기존 구간의 상행역과 하행역이 변경되어야 한다")
    void should_rebase_section_added_with_downStation() {
        Sections sections = new Sections();
        sections.addSection(createSection("1번역", "3번역", 100));
        sections.addSection(createSection("3번역", "5번역", 50));

        // when
        sections.addSection(createSection("4번역", "5번역", 30));

        // then
        assertThat(sections.getStations()).containsExactlyElementsOf(createStations("1번역", "3번역", "4번역", "5번역"));
    }

    @Test
    @DisplayName("기존 구간에서 특정 구간을 지우게 되면, 특정 구간을 제외한 역들이 정상적으로 연결되어야 한다")
    void should_replace_with_other_section_when_remove_section() {
        // given
        Sections sections = new Sections();
        sections.addSection(createSection("1번역", "3번역", 100));
        sections.addSection(createSection("3번역", "5번역", 50));
        sections.addSection(createSection("5번역", "10번역", 100));

        // when
        sections.removeStation(createStation("3번역"));

        // then

        assertThat(sections.getStations()).containsExactlyElementsOf(createStations("1번역", "5번역", "10번역"));
    }

    @Test
    @DisplayName("기존 구간에서 상행 종점 구간을 지우게 되면, 종점 구간을 제외한 역들이 정상적으로 연결되어야 한다")
    void should_replace_with_other_section_when_remove_last_up_section() {
        // given
        Sections sections = new Sections();
        sections.addSection(createSection("1번역", "3번역", 100));
        sections.addSection(createSection("3번역", "5번역", 50));

        // when
        sections.removeStation(createStation("1번역"));

        // then

        assertThat(sections.getStations()).containsExactlyElementsOf(createStations("3번역", "5번역"));
    }

    @Test
    @DisplayName("기존 구간에서 하행 종점 구간을 지우게 되면, 종점 구간을 제외한 역들이 정상적으로 연결되어야 한다")
    void should_replace_with_other_section_when_remove_last_down_section() {
        // given
        Sections sections = new Sections();
        sections.addSection(createSection("1번역", "3번역", 100));
        sections.addSection(createSection("3번역", "5번역", 50));

        // when
        sections.removeStation(createStation("5번역"));

        // then

        assertThat(sections.getStations()).containsExactlyElementsOf(createStations("1번역", "3번역"));
    }

    @Test
    @DisplayName("구간이 하나일 경우, 종점역을 지우게 되면, 에러가 발생해야 한다")
    void throws_exception_when_remove_last_station_of_last_section() {
        // given
        Sections sections = new Sections();
        sections.addSection(createSection("1번역", "3번역", 100));

        // when && then
        assertThatThrownBy(() -> sections.removeStation(createStation("1번역")))
                .isInstanceOf(IllegalArgumentException.class);
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
