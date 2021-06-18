package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @DisplayName("동일한 상행역인지 확인")
    @Test
    void isSameUpStation() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색");

        Section 구간 = new Section(지하철2호선, 강남역, 잠실역, 200);

        assertTrue(구간.isSameUpStation(강남역));
    }

    @DisplayName("동일한 하행역인지 확인")
    @Test
    void isSameDownStation() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Line 지하철2호선 = new Line("2호선", "초록색");

        Section 구간 = new Section(지하철2호선, 강남역, 잠실역, 200);

        assertTrue(구간.isSameDownStation(잠실역));
    }
}