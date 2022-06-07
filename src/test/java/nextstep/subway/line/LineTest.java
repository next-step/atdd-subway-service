package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    Station upStation;
    Station downStation;
    Line line;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행선");
        downStation = new Station("하행선");
        line = new Line("2호선", "green", upStation, downStation, 10);
    }

    @Test
    void findUpStation() {
        Station newStation = new Station("신규역");
        line.getSections().add(new Section(line, newStation, upStation, 5));
        assertThat(line.findUpStation()).isEqualTo(newStation);
    }
}
