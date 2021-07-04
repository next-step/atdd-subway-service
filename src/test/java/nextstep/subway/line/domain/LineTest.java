package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    private Station 강남역;
    private Station 사당역;
    private Line 이호선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        사당역 = new Station("사당역");
        이호선 = new Line("2호선","green", 강남역, 사당역, 10);
    }


    @DisplayName("노선의 역들을 가져온다")
    @Test
    public void getStationsTest() {
        //given

        //when
        List<Station> stations = 이호선.getStations();

        //then
        assertThat(stations).containsExactly(강남역,사당역);
    }

    @DisplayName("노선에 중간역을 추가한다")
    @Test
    public void addStationInMiddle() {
        //given
        Station 교대역 = new Station("교대역");

        //when
        이호선.addStation(강남역, 교대역, 5);

        //then
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역, 사당역);
    }

    @DisplayName("노선에 상행종점을 추가한다")
    @Test
    public void addStationOnTop() {
        //given
        Station 교대역 = new Station("교대역");

        //when
        이호선.addStation(교대역, 강남역, 5);

        //then
        assertThat(이호선.getStations()).containsExactly(교대역, 강남역, 사당역);
    }

    @DisplayName("노선에 하행종점 추가한다")
    @Test
    public void addStationBelow() {
        //given
        Station 교대역 = new Station("교대역");

        //when
        이호선.addStation(사당역, 교대역, 5);

        //then
        assertThat(이호선.getStations()).containsExactly(강남역, 사당역, 교대역);
    }

    @DisplayName("노선에 상행역을 삭제한다")
    public void removeUpStation() {
        //given
        Station 교대역 = new Station("교대역");
        이호선.addStation(강남역, 교대역, 5);
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역, 사당역);

        //when
        이호선.removeStation(강남역);

        //then
        assertThat(이호선.getStations()).containsExactly(교대역, 사당역);
    }

    @DisplayName("노선에 중간역을 삭제한다")
    @Test
    public void removeMiddleStation() {
        //given
        Station 교대역 = new Station("교대역");
        이호선.addStation(강남역, 교대역, 5);
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역, 사당역);

        //when
        이호선.removeStation(교대역);

        //then
        assertThat(이호선.getStations()).containsExactly(강남역, 사당역);
    }

    @DisplayName("노선에 하행역을 삭제한다")
    @Test
    public void removeBelowStation() {
        //given
        Station 교대역 = new Station("교대역");
        이호선.addStation(강남역, 교대역, 5);
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역, 사당역);

        //when
        이호선.removeStation(사당역);

        //then
        assertThat(이호선.getStations()).containsExactly(강남역, 교대역);
    }


}
