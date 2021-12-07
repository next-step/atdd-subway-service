package study.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
class UnitTest {
    @Test
    void update() {
        // given
        String newName = "구분당선";

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = Line.of("신분당선", "RED", upStation, downStation, 10);

        // when
        line.updateName(newName);
        line.updateName("GREEN");

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }
}
