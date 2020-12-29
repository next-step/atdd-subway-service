package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.ExploreSectionException;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    @DisplayName("등록된 구간의 상행종점역을 찾을 수 있다.")
    @Test
    void findFirstStationTest() {
        String name = "2호선";
        String color = "초록색";
        int distance = 3;
        Line line = new Line(name, color, StationFixtures.강남역, StationFixtures.잠실역, distance);

        Station firstStation =  line.findUpStation();

        assertThat(firstStation.getName()).isEqualTo(StationFixtures.강남역.getName());
    }

    @DisplayName("등록된 구간이 없는 Line의 상행종점역을 찾으면 예외 발생")
    @Test
    void findFirstStationFailTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);

        assertThatThrownBy(line::findUpStation)
                .isInstanceOf(ExploreSectionException.class)
                .hasMessage("해당 노선의 첫번째 구간을 찾을 수 없습니다.");
    }

    @DisplayName("구간 순서대로 정렬된 역 목록을 받을 수 있다.")
    @Test
    void getStationsTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);
        List<Section> sections = Arrays.asList(
                new Section(line, StationFixtures.역삼역, StationFixtures.삼성역, 5),
                new Section(line, StationFixtures.삼성역, StationFixtures.잠실역, 5),
                new Section(line, StationFixtures.강남역, StationFixtures.역삼역, 5)
        );
        Line sectionAddedLine = new Line(name, color, sections);

        List<Station> stations = sectionAddedLine.getStations();

        assertThat(stations.get(0)).isEqualTo(StationFixtures.강남역);
        assertThat(stations.get(stations.size() - 1)).isEqualTo(StationFixtures.잠실역);
    }

    @DisplayName("구간내 등록된 역이 없으면 역 목록 조회 시 빈 배열을 받는다.")
    @Test
    void getEmptyStationsTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);

        List<Station> stations = line.getStations();

        assertThat(stations).hasSize(0);
    }

    @DisplayName("다음 Section을 탐색할 수 있다.")
    @Test
    void findNextSectionTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);
        Section firstSection = new Section(line, StationFixtures.강남역, StationFixtures.역삼역, 5);
        Section secondSection = new Section(line, StationFixtures.역삼역, StationFixtures.삼성역, 5);
        List<Section> sections = Arrays.asList(
                secondSection,
                new Section(line, StationFixtures.삼성역, StationFixtures.잠실역, 5),
                firstSection
        );
        Line sectionAddedLine = new Line(name, color, sections);

        Section nextSection = sectionAddedLine.findNextSection(firstSection);

        assertThat(nextSection.getUpStation()).isEqualTo(secondSection.getUpStation());
        assertThat(nextSection.getDownStation()).isEqualTo(secondSection.getDownStation());
    }
}