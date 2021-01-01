package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Sesions 일급 객체 Test Class")
class SectionsTest {

    private Sections sections;

    private Line line = new Line("2호선", "yellow");
    private Station 강남역 = new Station("강남역");
    private Station 사당역 = new Station("사당역");

    @BeforeEach
    void setup() {
        sections = new Sections();
    }

    @DisplayName("Section 추가 Test")
    @Test
    void addSectionTest() {
        Section section = new Section(line, 강남역, 사당역, 10);

        sections.add(section);
        List<Station> stations = sections.getStations();

        assertThat(stations).isNotEmpty();
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(0).getName()).isEqualTo("강남역");
        assertThat(stations.get(1).getName()).isEqualTo("사당역");
    }

    @DisplayName("Section 중복 추가 Test")
    @Test
    void addDuplicateSectionTest() {
        Section section = new Section(line, 강남역, 사당역, 10);

        sections.add(section);

        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 등록된 구간 입니다.");
    }

    @DisplayName("Line에 추가되지 않은 역의 Section 추가 Test")
    @Test
    void addNotContainsSectionTest() {
        Section section = new Section(line, 강남역, 사당역, 10);

        sections.add(section);
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");
        Section notContainsSection = new Section(line, 양재역, 판교역, 10);

        assertThatThrownBy(() -> sections.add(notContainsSection))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("Section 삭제 Test")
    @Test
    void removeSectionTest() {
        Station 양재역 = new Station("양재역");
        Section section01 = new Section(line, 강남역, 사당역, 10);
        Section section02 = new Section(line, 사당역, 양재역, 10);

        sections.add(section01);
        sections.add(section02);

        sections.remove(line, 사당역);

        List<Station> stations = sections.getStations();
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(0).getName()).isEqualTo("강남역");
    }

    @DisplayName("비어있는 Sections에 삭제 Test")
    @Test
    void removeEmptySectionsTest() {
        assertThatThrownBy(() -> sections.remove(line, 강남역))
                .isInstanceOf(RuntimeException.class);
    }

}
