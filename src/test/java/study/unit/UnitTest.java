package study.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
public class UnitTest {
    @Test
    void update() {
        // given
        String newName = "구분당선";

        Station upStation = new Station("강남역");
        Station downStation = new Station("광교역");
        Line line = new Line(1L, "신분당선", "RED");
        Section section = new Section(upStation, downStation, 10);
        line.addSection(section);
        Line newLine = new Line(2L, newName, "GREEN");

        // when
        line.update(newLine.getName(), newLine.getColor());

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }
}
