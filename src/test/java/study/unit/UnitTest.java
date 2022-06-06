package study.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트")
class UnitTest {
    @Test
    void update() {
        // given
        String newName = "구분당선";

        Station upStation = Station.builder("강남역")
                .build();
        Station downStation = Station.builder("광교역")
                .build();
        Line line = Line.builder("신분당선", "RED", upStation, downStation, Distance.valueOf(10))
                .build();
        Line newLine = Line.builder(newName, "GREEN", upStation, downStation, Distance.valueOf(10))
                .build();

        // when
        line.update(newLine);

        // then
        assertThat(line.name()).isEqualTo(newName);
    }
}
