package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private Station 강남역;
    private Station 교대역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        이호선 = new Line("2호선", "green", 강남역, 교대역, 10);
    }

    @Test
    @DisplayName("노선에 속한 역을 찾는다")
    void findStationTest() {

        // when
        List<Station> stations = 이호선.getStations();

        // then
        assertThat(stations).hasSize(2);
    }
    
    @Test
    @DisplayName("노선에 속한 역을 제거한다")
    void removeStationTest() {

        // given
        Station 서초역 = new Station("서초역");
        이호선.addSection(new Section(이호선, 교대역, 서초역, 5));

        // when
        이호선.removeStation(서초역);

        // then
        assertThat(이호선.getStations()).hasSize(2);
    }

}