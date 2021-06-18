package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @DisplayName("지하철역이 순서대로 조회되는지 확인")
    @Test
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색", 강남역, 잠실역, 200);

        Section 구간 = new Section(강남역, 삼성역, 100);

        지하철2호선.addSection(구간);
        assertThat(지하철2호선.getStations()).containsExactly(강남역, 삼성역, 잠실역);
    }
}