package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {


    @Test
    @DisplayName("노선의 포함된 역들을 가져온다")
    void getStations() {
        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Line line = new Line("8호선", "분홍색", 잠실역, 문정역, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).contains(잠실역, 문정역);

    }
}
