package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선 기능 테스트")
class LineTest {
    @Test
    @DisplayName("노선에 등록된 역 조회")
    void findStations() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = new Line("신분당선", "green", 강남역, 광교역, 30);

        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }
}
