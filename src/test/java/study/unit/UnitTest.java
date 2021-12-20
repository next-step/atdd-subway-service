package study.unit;

import nextstep.subway.line.domain.*;
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
        Section section = Section.of(upStation, downStation, Distance.of(10));
        Line line = Line.of("신분당선", "RED", Sections.from(section), Fare.from(900));
        Line newLine = Line.of(newName, "GREEN", Fare.from(900));

        // when
        line.update(newLine);

        // then
        assertThat(line.getName()).isEqualTo(newName);
    }
}
