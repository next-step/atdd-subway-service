package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련")
public class SectionsTest {
    private Station 강남역;
    private Station 사당역;
    private Line 이호선;
    private Sections 이호선_최초구간;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        사당역 = new Station("사당역");

        이호선 = new Line("2호선","green", 강남역, 사당역, 10);
        이호선_최초구간 = 이호선.getSectionsNew();

    }

    @DisplayName("역들 조회")
    @Test
    public void getStationsTest() {

        //when
        List<Station> stations = 이호선_최초구간.getStations();

        //then
        assertThat(stations).containsExactly(강남역,사당역);
    }

}
