package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 entity 테스트")
public class LineTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "red", 강남역, 광교역, 10);
    }

    @Test
    void 지하철역_리스트_반환() {
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 광교역);
    }
}
