package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@DataJpaTest
public class LineTest {
    @DisplayName("노선 정보 생성")
    @Test
    void createLine() {
        // given
        Line line = new Line("경춘선", "red");

        // when
        String lineName = line.getName();
        String lineColor = line.getColor();

        // then
        assertThat(lineName).isEqualTo("경춘선");
        assertThat(lineColor).isEqualTo("red");

    }

    @DisplayName("지하철 역 정보를 포함하는 노선 정보 생성")
    @Test
    void createStationContainsLine() {
        // given
        Station upStation = new Station(1L, "1번역");
        Station downStation = new Station(2L, "2번역");
        Line line = new Line("경춘선", "red", upStation, downStation, 10);

        // when
        List<Station> stations = line.getSortedStations();

        // then
        assertThat(stations).contains(upStation, downStation);

    }

    @DisplayName("노선 정보 수정")
    @Test
    void updateLine() {
        // given
        Line targetLine = new Line("경춘선", "red");
        Line updateLine = new Line("신분당선", "blue");
        targetLine.update(updateLine);

        // when
        String name = targetLine.getName();
        String color = targetLine.getColor();

        // then
        assertThat(name).isEqualTo("신분당선");
        assertThat(color).isEqualTo("blue");

    }

    @DisplayName("노선에 역 추가")
    @Test
    void addLineStation() {
        // given

        // when

        // then

    }

    @DisplayName("노선에 역 삭제")
    @Test
    void removeLineStation() {
        // given

        // when

        // then

    }
}
