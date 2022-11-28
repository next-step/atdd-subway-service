package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    private Line 역2개_2호선;
    private Line 역3개_2호선;
    private Station 서초역;
    private Station 강남역;
    private Station 교대역;

    @BeforeEach
    void setUp() {
        서초역 = new Station("서초역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역2개_2호선 = createLine("2호선", 서초역, 강남역, 10);
        역3개_2호선 = createLine("2호선", 서초역, 강남역, 10);
        역3개_2호선.addSection(new Section(서초역, 교대역, 5));
    }

    @DisplayName("노선에 할당된 역을 조회할 수 있다.")
    @Test
    void findAssignedStations() {
        assertThat(역2개_2호선.findAssignedStations()).containsExactly(서초역, 강남역);
    }

    @DisplayName("구간을 추가하고 노선 조회 시 추가한 역이 조회된다.")
    @Test
    void addSection() {
        Section newSection = new Section(서초역, 교대역, 5);

        역2개_2호선.addSection(newSection);

        assertThat(역2개_2호선.findAssignedStations()).containsExactly(서초역, 교대역, 강남역);
    }

    @DisplayName("지하철 역을 제거하고 노선 조회 시 제거한 역이 조회되지 않는다.")
    @Test
    void deleteStation() {
        역3개_2호선.deleteStation(교대역);

        assertThat(역3개_2호선.findAssignedStations()).containsExactly(서초역, 강남역);
    }
}
