package nextstep.subway.line.acceptance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선")
class LineTest {
    @Test
    @DisplayName("노선에 포함된 모든 역을 조회할 수 있다.")
    void 노선_역_조회() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");

        Line 이호선 = new Line("2호선", "green", 강남역, 역삼역, 5);
        assertThat(이호선.getStations()).containsExactlyInAnyOrder(강남역, 역삼역);
    }
}
