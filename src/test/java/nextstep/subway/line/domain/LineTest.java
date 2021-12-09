package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
    }

    @DisplayName("역 목록 가져오기")
    @Test
    void getStations() {
        Line line = new Line("신분당선", "red", 강남역, 광교역, 100);

        Stations stations = line.getStations();

        assertThat(stations).isEqualTo(new Stations(Arrays.asList(강남역, 광교역)));
    }
}