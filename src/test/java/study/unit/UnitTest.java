package study.unit;

import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Name;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("단위 테스트")
public class UnitTest {
    @Test
    void update() {
        // given
        String newName = "구분당선";
        String newColor = "GREEN";

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = new Line("신분당선", "RED", upStation, downStation, 10);

        // when
        line.updateNameAndColor(newName, newColor);

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo(Name.from(newName)),
                () -> assertThat(line.getColor()).isEqualTo(Color.from(newColor))
        );
    }
}
