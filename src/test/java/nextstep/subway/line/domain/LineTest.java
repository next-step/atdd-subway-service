package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    private Line 역2개_2호선;
    private Station 서초역;
    private Station 강남역;
    private Station 교대역;

    @BeforeEach
    void setUp() {
        서초역 = new Station("서초역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역2개_2호선 = Line.builder().upStation(서초역).downStation(강남역).distance(10).build();
    }

    @DisplayName("노선에 할당된 역을 조회할 수 있다.")
    @Test
    void findAssignedStations() {
        assertThat(역2개_2호선.findAssignedStations()).containsExactly(서초역, 강남역);
    }
}
