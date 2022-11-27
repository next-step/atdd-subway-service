package study.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
public class UnitTest {
    @Test
    void update() {
        // given
        String newName = "구분당선";

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = createLine("신분당선", upStation, downStation, 10);
        Line newLine = createLine(newName, upStation, downStation, 10);

        // when
        line.changeName(newLine.getName());
        line.changeColor(newLine.getColor());

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }
}
