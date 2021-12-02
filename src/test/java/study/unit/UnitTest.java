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
        String newColor = "GREEN";

        Station upStation = Station.of(1L, "강남역");
        Station downStation = Station.of(2L, "광교역");
        Section section = Section.of(1L, upStation, downStation, 10);
        Line line = Line.of(1L, "신분당선", "RED", section);

        // when
        line.update(newName, newColor);

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }
}
