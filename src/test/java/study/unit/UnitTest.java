package study.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

@DisplayName("단위 테스트")
class UnitTest {
    @Test
    void update() {
        // given
        Name newName = Name.from( "구분당선" );
        Color newColor = Color.from("GREEN");

        Station upStation = Station.from("강남역");
        Station downStation = Station.from("광교역");
        Line line = Line.of(Name.from("신분당선"), Color.from("RED"),
            Sections.from(Section.of(upStation, downStation, Distance.from(10))));

        // when
        line.update(newName, newColor);

        // then
        assertThat(line.getName()).isEqualTo(newName.toString());
    }
}
