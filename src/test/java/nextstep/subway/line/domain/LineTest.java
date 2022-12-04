package nextstep.subway.line.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철 노선 도메인 단위테스트")
@SpringBootTest
public class LineTest {
    private Station 강남역;
    private Station 광교역;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = new Line("2호선","bg-green-600", 강남역, 광교역, 10);
    }

    @Test
    @DisplayName("기존 getStationsTest 로직 검증")
    void getStationsTest() {
        List<Station> stations = 신분당선.getStation();
        assertThat(stations.get(0)).isEqualTo(강남역);
        assertThat(stations.get(1)).isEqualTo(광교역);


    }
}
