package study.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineColor;
import nextstep.subway.line.domain.LineName;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
class UnitTest {

    @Test
    @DisplayName("노선명을 변경할 수 있다.")
    void update01() {
        // given
        String newName = "구분당선";

        Station upStation = Station.from("강남역");
        Station downStation = Station.from("광교역");
        Line line = Line.of("신분당선", "RED", upStation, downStation, 10);
        Line newLine = Line.of(newName, "GREEN");

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(LineName.from(newName));
    }

    @Test
    @DisplayName("노선색상을 변경할 수 있다.")
    void update02() {
        // given
        String newColor = "BLUE";

        Station upStation = Station.from("강남역");
        Station downStation = Station.from("광교역");
        Line line = Line.of("신분당선", "RED", upStation, downStation, 10);
        Line newLine = Line.of("신신분당선", newColor);

        // when
        line.update(newLine);

        // then
        assertThat(line.getColor()).isEqualTo(LineColor.from(newColor));
    }
}
