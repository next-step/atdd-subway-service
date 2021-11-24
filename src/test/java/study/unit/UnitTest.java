package study.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트")
class UnitTest {

    @Test
    @DisplayName("지하철 노선 수정")
    void update() {
        // given
        Name newName = Name.from("구분당선");
        Color newColor = Color.from("GREEN");

        Station upStation = Station.from(Name.from("강남역"));
        Station downStation = Station.from(Name.from("광교역"));
        Line line = Line.of(Name.from("신분당선"), Color.from("RED"),
            Sections.from(Section.of(upStation, downStation, Distance.from(10))));

        // when
        line.update(newName, newColor);

        // then
        assertThat(line.name())
            .isEqualTo(newName);
    }
}
