package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    @DisplayName("상행역에서 하행역 순으로 정렬하여 역을 가져온다.")
    @Test
    void getStationsOrderBy() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);

        List<Station> stationsOrderBy = 신분당선.getStationsInOrder();
        assertThat(stationsOrderBy).isEqualTo(Arrays.asList(강남역, 광교역));
    }
}
