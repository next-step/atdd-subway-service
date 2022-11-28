package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    @DisplayName("노선에 포함되어 있는 역 리스트 가져오기")
    @Test
    void getStations() {
        Line line = new Line("신분당선", "빨간색", new Station("강남역"), new Station("광교역"), 10);

        List<Station> stations = line.getStations();

        assertThat(stations).containsExactlyElementsOf(Arrays.asList(
                new Station("강남역"),
                new Station("광교역")
        ));
    }
}
