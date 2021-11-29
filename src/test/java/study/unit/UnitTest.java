package study.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
class UnitTest {
    private static final int FARE_1000 = 1000;
    private static final int FARE_900 = 900;

    @Test
    void update() {
        // given
        String newName = "구분당선";

        Station upStation = Station.from("강남역");
        Station downStation = Station.from("광교역");
        Line line = Line.of("신분당선", "RED", FARE_1000, upStation, downStation, 10);
        Line newLine = Line.of(newName, "GREEN", FARE_1000);

        // when
        line.update(newLine);

        // then
        assertThat(line.getName().getValue()).isEqualTo(newName);
    }
}
