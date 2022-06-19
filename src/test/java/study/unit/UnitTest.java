package study.unit;

import nextstep.subway.line.domain.Distance;
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
        String newColor = "GREEN";
        Integer newExtraFare = 900;

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = new Line("신분당선", "RED", 0, upStation, downStation, new Distance(10));
        Line newLine = new Line(newName, newColor, newExtraFare);

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(newName);
        assertThat(line.getColor()).isEqualTo(newColor);
        assertThat(line.getExtraFare()).isEqualTo(newExtraFare);
    }
}
