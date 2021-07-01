package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("Line에 존재하는 역들을 추출한다.")
    void getStations_test() {
        Station 강남역 = new Station("강남역");
        Station 왕십리역 = new Station("왕십리역");
        Line 첫번째_구간 = new Line("2호선", "red", 강남역, 왕십리역, 10);

        List<Station> 지하철역들 = 첫번째_구간.getStations();

        assertThat(지하철역들).containsExactly(
                강남역,
                왕십리역
        );
    }
}
