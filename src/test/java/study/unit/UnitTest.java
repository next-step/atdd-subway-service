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

        Station upStation = Station.from("강남역");
        Station downStation = Station.from("광교역");
        Line line = new Line.Builder("신분당선", "RED")
                .section(Section.of(upStation, downStation, 10))
                .build();
        Line newLine = new Line.Builder(newName, "GREEN").build();

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }
}
