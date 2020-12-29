package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.CannotFindLineEndUpStationException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    @DisplayName("등록된 구간의 상행종점역을 찾을 수 있다.")
    @Test
    void findFirstStationTest() {
        String name = "2호선";
        String color = "초록색";
        int distance = 3;
        Station upStation = new Station("강남역");
        Station downStation = new Station("서초역");
        Line line = new Line(name, color, upStation, downStation, distance);

        Station firstStation =  line.findUpStation();

        assertThat(firstStation.getName()).isEqualTo(upStation.getName());
    }

    @DisplayName("등록된 구간이 없는 Line의 상행종점역을 찾으면 예외 발생")
    @Test
    void findFirstStationFailTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);

        assertThatThrownBy(line::findUpStation)
                .isInstanceOf(CannotFindLineEndUpStationException.class)
                .hasMessage("해당 노선의 상행종점역을 찾을 수 없습니다.");
    }
}