package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LineTest {
    private Station 강남역;
    private Station 종합운동장역;
    private Station 잠실역;
    Line 호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        종합운동장역 = new Station("종합운동장역");
        잠실역 = new Station("잠실역");
        호선 = new Line("2호선", "green", 강남역, 잠실역, 10);
    }

    @Test
    void 노선_조회() {
        // then
        assertThat(호선.findStations()).contains(강남역, 잠실역);
    }

    // 노선 추가

    // 노선 삭제

}
