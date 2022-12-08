package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LinesTest {

    private Line 칠호선;
    private Line 오호선;
    private Line 구호선;
    private Station 강동구청역;
    private Station 천호역;
    private Station 강동역;
    private Station 둔촌역;
    private Station 올림픽공원역;

    @BeforeEach
    public void setUp() {
        강동구청역 = new Station(2l, "강동구청역");
        천호역 = new Station(4l, "천호역");
        강동역 = new Station(12l, "강동역");
        둔촌역 = new Station(16l, "둔촌역");
        올림픽공원역 = new Station(18l, "올림픽공원역");
        칠호선 = new Line("칠호선", "pink", new Station(1l, "몽촌토성역"), 강동구청역, 3, 1000);
        칠호선.addSection(강동구청역, 천호역, 3);
        오호선 = new Line("오호선", "purple", 천호역, 강동역, 3, 4000);
        오호선.addSection(강동역, 둔촌역, 5);
        오호선.addSection(둔촌역, 올림픽공원역, 5);
        구호선 = new Line("구호선", "gold", new Station(41l, "한성백제역"), 올림픽공원역, 3, 1000);

    }

    @DisplayName("지하철 노선목록으로 부터 가장 요금을 가지고있는 노선을 반환")
    @Test
    void findMaxLine() {
        Lines lines = Lines.of(Arrays.asList(칠호선, 오호선, 구호선));
        List<Station> stations = Arrays.asList(강동구청역, 천호역, 강동역, 둔촌역, 올림픽공원역);

        int fare = lines.getMaxFareByStations(stations);

        assertThat(fare).isEqualTo(4000);
    }

}
