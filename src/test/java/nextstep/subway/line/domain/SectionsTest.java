package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {
    private Sections sections;
    private Line line;

    @BeforeEach
    void setUp() {
        Station upStation = new Station("공덕역");
        Station downStation = new Station("마포역");
        line = new Line("오호선", "purple");
        sections = new Sections();
        sections.addSection(new Section(line, upStation, downStation, 10));
    }

    @Test
    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있으면 구간을 추가할 수 없다.")
    void addSectionWithAlreadyExistingBothStations() {
        Station upStation = new Station("공덕역");
        Station downStation = new Station("마포역");
        Section newSection = new Section(line, upStation, downStation, 5);

        assertThatThrownBy(() -> {
            sections.addSection(newSection);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없다.")
    void addSectionWithNoExistingStations() {
        Station upStation = new Station("여의나루");
        Station downStation = new Station("여의도역");
        Section newSection = new Section(line, upStation, downStation, 5);

        assertThatThrownBy(() -> {
            sections.addSection(newSection);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행역이 같은 구간을 등록하면 역 사이에 새로운 역이 등록된다.")
    void addSectionWithSameUpStation() {
        Station upStation = new Station("공덕역");
        Station downStation = new Station("여의나루역");
        Section newSection = new Section(line, upStation, downStation, 5);

        sections.addSection(newSection);

        Set<Station> stations = sections.findAllStations();
        List<String> stationNames = stations.stream().map(Station::getName).collect(Collectors.toList());
        assertThat(stationNames).containsAll(Arrays.asList("공덕역", "마포역", "여의나루역"));
    }

    @Test
    @DisplayName("하행역이 같은 구간을 등록하면 역 사이에 새로운 역이 등록된다.")
    void addSectionWithSameDownStation() {
        Station upStation = new Station("애오개역");
        Station downStation = new Station("마포역");
        Section newSection = new Section(line, upStation, downStation, 5);

        sections.addSection(newSection);

        Set<Station> stations = sections.findAllStations();
        List<String> stationNames = stations.stream().map(Station::getName).collect(Collectors.toList());
        assertThat(stationNames).containsAll(Arrays.asList("애오개역", "공덕역", "마포역"));
    }

    @Test
    @DisplayName("역 사이에 새로운 역을 등록할 때, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.")
    void addSectionWithSameDistance() {
        Station upStation = new Station("애오개역");
        Station downStation = new Station("마포역");
        Section newSection = new Section(line, upStation, downStation, 10);

        assertThatThrownBy(() -> {
            sections.addSection(newSection);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
