package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("노선에 역이 없는 경우 역 목록은 비어 있다")
    void getStations1() {

        // given
        Line line = new Line("신분당선", "bg-red-600");

        // when
        final List<Station> result = line.getStations();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("노선에서 역 목록 가져오기")
    void getStations2() {

        // given
        Station upStation = Station.of("강남역");
        Station downStation = Station.of("광교역");
        Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        // when
        final List<Station> result = line.getStations();

        // then
        assertThat(result).contains(upStation, downStation);
    }

}