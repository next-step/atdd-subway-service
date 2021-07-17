package study.unit;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.fare.domain.Fare.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
class UnitTest {

    @Test
    void update() {
        // given
        final String NEW_NAME = "구분당선";
        final String NEW_COLOR = "GREEN";

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = new Line("신분당선", "RED", upStation, downStation, new Distance(10), DEFAULT_FARE);
        Line newLine = new Line(NEW_NAME, NEW_COLOR, new Fare(100));

        // when
        line.update(NEW_NAME, NEW_COLOR);

        // then
        assertThat(line.getName()).isEqualTo(NEW_NAME);
        assertThat(line.getColor()).isEqualTo(NEW_COLOR);
    }
}
