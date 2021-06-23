package nextstep.subway.line.domain;

import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("노선의 역들을 가져온다")
    void getStations() {
        Line 강남_광교 = new Line("신분당선", "레드", 강남역, 광교역, 100);
        assertThat(강남_광교.getStations())
            .containsExactly(강남역, 광교역);
    }
}
