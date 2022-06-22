package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SectionsTest {
    private Sections sections;
    private Line line;

    @BeforeEach
    void setUp() {
        sections = new Sections();
        line = new Line("2호선", "bg-blue-400");
        sections.add(new Section(line, new Station("강남역"), new Station("잠실역"), 10));
    }

    @Test
    @DisplayName("전체 역 조회")
    void getAllStations() {
        // when
        List<Station> stations = sections.getAllStations();
        // then
        assertThat(stations).hasSize(2);
    }

    @Test
    @DisplayName("섹션 추가")
    void add() {
        // given
        Section newSection = new Section(line, new Station("강남역"), new Station("방배역"), 5);
        // when
        sections.add(newSection);
        // then
        assertThat(sections.getSections()).contains(newSection);
    }

    @Test
    @DisplayName("추가 불가 섹션 오류")
    void can_not_add_section() {
        // given
        Section newSection = new Section(line, new Station("사당역"), new Station("신림역"), 5);
        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(LineException.class);
    }

    @Test
    @DisplayName("존재하는 섹션 추가 오류")
    void both_exist_section() {
        // given
        Section newSection = new Section(line, new Station("강남역"), new Station("잠실역"), 5);
        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(LineException.class);
    }

    @Test
    @DisplayName("등록된 Section이 1개일때 삭제 오류")
    void delete_one_section() {
        // then
        assertThatThrownBy(() -> sections.delete(new Station("강남역")))
                .isInstanceOf(LineException.class);
    }

    @Test
    @DisplayName("종착역 삭제")
    void delete_end_station() {
        // given
        sections.add(new Section(line, new Station("강남역"), new Station("방배역"), 5));
        // when
        sections.delete(new Station("강남역"));
        // then
        assertThat(sections.getAllStations()).hasSize(2);
        assertThat(sections.getAllStations()).doesNotContain(new Station("강남역"));
    }
    
    @Test
    @DisplayName("중간역 삭제")
    void delete_middle_station() {
        // given
        sections.add(new Section(line, new Station("강남역"), new Station("방배역"), 5));
        // when
        sections.delete(new Station("방배역"));
        // then
        assertThat(sections.getAllStations()).hasSize(2);
        assertThat(sections.getAllStations()).doesNotContain(new Station("보라매역"));
    }

    @Test
    @DisplayName("정렬된 지하철역 조회")
    void get_sorted_stations() {
        // given
        sections.add(new Section(line, new Station("강남역"), new Station("방배역"), 5));
        sections.add(new Section(line, new Station("사당역"), new Station("강남역"), 5));
        // when
        List<String> stationName = sections.getSortedStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        // then
        assertThat(stationName).containsExactlyElementsOf(Arrays.asList("사당역", "강남역", "방배역", "잠실역"));
    }

}
