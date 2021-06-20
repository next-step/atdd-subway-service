package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @DisplayName("노선에 포함된 역 목록 조회")
    @Test
    void stations() {
        // given
        Station 강남역 = new Station("강남역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "red", 강남역, 정자역, 35);

        // when
        List<Station> stations = 신분당선.stations();

        // then
        assertThat(stations.size()).isEqualTo(2);
    }
}