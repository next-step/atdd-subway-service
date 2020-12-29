package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.ExploreSectionException;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineSectionExplorerTest {
    @DisplayName("등록된 구간의 상행종점역을 찾을 수 있다.")
    @Test
    void findFirstStationTest() {
        String name = "2호선";
        String color = "초록색";
        int distance = 3;
        Line line = new Line(name, color, StationFixtures.강남역, StationFixtures.잠실역, distance);
        LineSectionExplorer lineSectionExplorer = new LineSectionExplorer(line.getSections());

        Station firstStation = lineSectionExplorer.findUpStation();

        assertThat(firstStation.getName()).isEqualTo(StationFixtures.강남역.getName());
    }

    @DisplayName("등록된 구간이 없는 Line의 상행종점역을 찾으면 예외 발생")
    @Test
    void findFirstStationFailTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);

        LineSectionExplorer lineSectionExplorer = new LineSectionExplorer(line.getSections());

        assertThatThrownBy(lineSectionExplorer::findUpStation)
                .isInstanceOf(ExploreSectionException.class)
                .hasMessage("해당 노선의 첫번째 구간을 찾을 수 없습니다.");
    }
}