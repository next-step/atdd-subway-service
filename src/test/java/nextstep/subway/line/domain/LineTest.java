package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Line line;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        upStation = new Station("강남역");
        downStation = new Station("판교역");

        line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);
    }

    @Test
    void 모든_역을_가져온다() {
        // when
        List<Station> stations = line.getStations();
        // then
        assertThat(stations).hasSize(2);
    }

    @Test
    void 삭제할_역이_포함된_구간을_삭제한다() {
        // given
        Station station = new Station("강남역");

        // when
        line.removeSection(station);

        // then
        assertThat(line.getSections()).isEmpty();
    }
}
