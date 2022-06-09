package nextstep.subway.line.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineTest {

    private Line line;
    private Station 잠실역;
    private Station 강남역;

    @BeforeEach
    void before() {
        잠실역 = new Station("잠실역");
        강남역 = new Station("강남역");
        line = new Line("2호선", "green", 잠실역, 강남역,10);
    }

    @Test
    void getStations() {
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(잠실역, 강남역);
    }
}
