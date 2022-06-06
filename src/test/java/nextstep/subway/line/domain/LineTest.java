package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    void 상행역에서_하행역까지_모든_역을_가져온다() {
        // when
        List<Station> stations = line.getStations();
        // then
        assertAll(
                () -> assertThat(stations.get(0)).isEqualTo(upStation),
                () -> assertThat(stations.get(1)).isEqualTo(downStation)
        );
    }

}
