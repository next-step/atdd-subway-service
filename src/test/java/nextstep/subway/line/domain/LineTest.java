package nextstep.subway.line.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 도메인 단위테스트")
public class LineTest {
    private Station 강남역;
    private Station 광교역;
    private Station 정자역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        정자역 = new Station("정자역");
        신분당선 = new Line("2호선", "bg-green-600", 강남역, 광교역, 10);
    }

    @Test
    @DisplayName("getStations 로직 검증")
    void getStationsTest() {
        List<Station> stations = 신분당선.getStations();
        assertThat(stations.get(0)).isEqualTo(강남역);
        assertThat(stations.get(1)).isEqualTo(광교역);
    }
    @Test
    @DisplayName("Section 제거 검증")
    void removeSectionTest() {
        Section 광교정자 = new Section(신분당선, 광교역, 정자역, 10);
        신분당선.addSection(광교정자);
        신분당선.removeStation(광교역);
        assertThat(신분당선.getStations()).isEqualTo(Arrays.asList(강남역, 정자역));
    }
}
