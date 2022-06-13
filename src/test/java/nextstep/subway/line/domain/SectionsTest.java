package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {
    private Sections sections;
    private Line line;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        upStation = new Station("공덕역");
        downStation = new Station("마포역");
        line = new Line("오호선", "purple");
        sections = new Sections();
        sections.addSection(Section.of(line, upStation, downStation, 10));
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있으면 구간을 추가할 수 없다.")
    void addSectionWithAlreadyExistingBothStations() {
        upStation = new Station("공덕역");
        downStation = new Station("마포역");
        Section newSection = Section.of(line, upStation, downStation, 5);

        assertThatThrownBy(() -> {
            sections.addSection(newSection);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없다.")
    void addSectionWithNoExistingStations() {
        upStation = new Station("여의나루");
        downStation = new Station("여의도역");
        Section newSection = Section.of(line, upStation, downStation, 5);

        assertThatThrownBy(() -> {
            sections.addSection(newSection);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행역이 같은 구간을 등록하면 역 사이에 새로운 역이 등록된다.")
    void addSectionWithSameUpStation() {
        upStation = new Station("공덕역");
        downStation = new Station("여의나루역");
        Section newSection = Section.of(line, upStation, downStation, 5);

        sections.addSection(newSection);

        List<Station> stations = sections.findAllStations();
        List<String> stationNames = stations.stream().map(Station::getName).collect(Collectors.toList());
        assertThat(stationNames).containsAll(Arrays.asList("공덕역", "마포역", "여의나루역"));
    }

    @Test
    @DisplayName("하행역이 같은 구간을 등록하면 역 사이에 새로운 역이 등록된다.")
    void addSectionWithSameDownStation() {
        upStation = new Station("애오개역");
        downStation = new Station("마포역");
        Section newSection = Section.of(line, upStation, downStation, 5);

        sections.addSection(newSection);

        List<Station> stations = sections.findAllStations();
        List<String> stationNames = stations.stream().map(Station::getName).collect(Collectors.toList());
        assertThat(stationNames).containsAll(Arrays.asList("애오개역", "공덕역", "마포역"));
    }

    @Test
    @DisplayName("역 사이에 새로운 역을 등록할 때, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.")
    void addSectionWithSameDistance() {
        upStation = new Station("애오개역");
        downStation = new Station("마포역");
        Section newSection = Section.of(line, upStation, downStation, 10);

        assertThatThrownBy(() -> {
            sections.addSection(newSection);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선에 A-B-C 역이 있을 때, B역을 제거하면 A-C로 재배치된다.")
    void removeSection() {
        upStation = new Station("마포역");
        downStation = new Station("여의나루역");
        Section newSection = Section.of(line, upStation, downStation, 10);
        sections.addSection(newSection);

        sections.remove(upStation);

        List<Section> sections = this.sections.getSections();
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("노선에 등록되지 않은 역을 제거하려고 하면 예외를 반환한다.")
    void removeNoExistingStation() {
        Station noExistingStation = new Station("여의나루역");

        assertThatThrownBy(() -> {
            sections.remove(noExistingStation);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구간이 하나인 노선에서 마지막 구간을 제거하려고 하면 예외를 반환한다.")
    void removeOnlyOneSection() {
        assertThatThrownBy(() -> {
            sections.remove(upStation);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("정렬된 역을 반환한다.")
    void findOrderedStations() {
        upStation = new Station("애오개역");
        downStation = new Station("마포역");
        Section newSection = Section.of(line, upStation, downStation, 5);
        sections.addSection(newSection);

        List<Station> orderedAllStations = sections.findOrderedAllStations();
        List<String> orderedAllStationsNames = orderedAllStations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(orderedAllStationsNames).containsExactly("공덕역", "애오개역", "마포역");
    }
}
