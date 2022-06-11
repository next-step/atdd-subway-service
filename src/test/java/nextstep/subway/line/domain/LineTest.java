package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class LineTest {

    private Station 강남역;
    private Station 광교역;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("신분당선의 지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // when
        List<Station> actual = 신분당선.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 광교역);
    }
}
