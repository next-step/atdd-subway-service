package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Line testLine;
    private Sections testSections;
    private Station testUpStation;
    private Station testDownStation;

    @BeforeEach
    void makeDefaultLine() {
        testLine = new Line("1호선", "navy");
        testUpStation = new Station("서울역");
        testDownStation = new Station("금정역");
        Section section = new Section(testLine, testUpStation, testDownStation, 10);
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section);
        testSections = new Sections(sectionList);
    }

    @DisplayName("노선내의 역 목록 조회")
    @Test
    void getStations() {
        assertThat(testSections.getStations()).containsExactly(testUpStation, testDownStation);
    }

    @DisplayName("노선 상에 상행종점역 추가")
    @Test
    void addUpStation() {
        Station newUpStation = new Station("의정부");
        Section newSection = new Section(testLine, newUpStation, testUpStation, 7);
        testSections.addSection(newSection);

        assertThat(testSections.getStations()).containsExactly(newUpStation, testUpStation, testDownStation);
    }

    @DisplayName("노선 상에 하행종점역 추가")
    @Test
    void addDownStation() {
        Station newDownStation = new Station("수원역");
        Section newSection = new Section(testDownStation, newDownStation, 7);
        testSections.addSection(newSection);

        assertThat(testSections.getStations()).containsExactly(testUpStation, testDownStation, newDownStation);
    }

    @DisplayName("노선 상에 중간역 추가")
    @Test
    void addMidStation() {
        Station newMidStation = new Station("안양역");
        Section newSection = new Section(newMidStation, testDownStation, 7);
        testSections.addSection(newSection);

        assertThat(testSections.getStations()).containsExactly(testUpStation, newMidStation, testDownStation);
    }

    @DisplayName("노선 상에 중간 역 삽입시 기존 구간보다 긴 거리를 삽입")
    @Test
    void addOverDistanceStation() {
        Station newMidStation = new Station("안양역");
        Section newSection = new Section(newMidStation, testDownStation, 15);

        assertThatThrownBy(() -> testSections.addSection(newSection))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("노선 상의 역 제거")
    @Test
    void removeLineStation() {
        Station newDownStation = new Station("수원역");
        Section newSection = new Section(testDownStation, newDownStation, 7);
        testSections.addSection(newSection);
        testSections.removeSection(testUpStation);

        assertThat(testSections.getStations()).containsExactly(testDownStation, newDownStation);
    }

    @DisplayName("노선 상에 존재하지 않는 역 제거")
    @Test
    void removeNotExistsStation() {
        Station otherStation = new Station("수원역");

        assertThatThrownBy(() -> testSections.removeSection(otherStation)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("노선 상행종점 조회")
    @Test
    void findUpStation() {
        assertThat(testSections.findFirstUpStation()).isEqualTo(testUpStation);
    }
}
