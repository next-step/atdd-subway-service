package study.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트")
public class UnitTest {

    @Test
    void update() {
        // given
        String newName = "구분당선";

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = 지하철_노선_생성("신분당선", "RED", upStation, downStation, 10);
        Line newLine = 지하철_노선_생성(newName, "GREEN");

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }

    private Line 지하철_노선_생성(String name, String color) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
