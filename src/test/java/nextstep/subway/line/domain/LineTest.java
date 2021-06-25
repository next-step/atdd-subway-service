package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("노선에 속한 역을 찾는다")
    void test() {
        // given
        Line line = new Line("2호선", "green", new Station("강남역"), new Station("교대역"), 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).hasSize(2);
    }
}