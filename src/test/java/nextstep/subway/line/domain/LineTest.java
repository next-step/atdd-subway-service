package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    Station 강남역;
    Station 광교역;
    Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "red", 강남역, 광교역, 10);
    }

    @DisplayName("노선에 속한 역들을 조회할 수 있다.")
    @Test
    void getStations() {
        List<Station> stations = 신분당선.getStations();

        assertThat(stations).containsExactly(강남역, 광교역);
    }

    @DisplayName("이미 등록된 구간을 등록할 경우 RuntimeException 이 발생한다.")
    @Test
    void already_exist_section() {

        assertThatThrownBy(() -> 신분당선.addLineStation(강남역, 광교역, 5)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 RuntimeException 이 발생한다.")
    @Test
    void none_exist_section() {
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        assertThatThrownBy(() -> 신분당선.addLineStation(양재역, 판교역, 5)).isInstanceOf(RuntimeException.class);

    }


    @DisplayName("역 사이에 새로운 역을 추가할 수 있다.")
    @Test
    void add_new_station() {
        Station 양재역 = new Station("양재역");

        신분당선.addLineStation(강남역, 양재역, 5);

        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 광교역);

    }

    @DisplayName("새로운 역을 상행 종점으로 등록할 수 있다.")
    @Test
    void add_up_station() {
        Station 논현역 = new Station("논현역");

        신분당선.addLineStation(논현역, 강남역, 10);

        assertThat(신분당선.getStations()).containsExactly(논현역, 강남역, 광교역);
    }


    @DisplayName("새로운 역을 상행 종점으로 등록할 수 있다.")
    @Test
    void add_down_station() {
        Station 호매실역 = new Station("호매실역");

        신분당선.addLineStation(광교역, 호매실역, 10);

        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역, 호매실역);
    }

    @DisplayName("구간이 하나인 노선에서 마지막 구간을 제거할때 RuntimeException 이 발생한다.")
    @Test
    void remove_last_one_section() {

        assertThatThrownBy(() -> 신분당선.removeLineStation(강남역)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("노선에서 역을 제거할 수 있다.")
    @Test
    void remove_station() {
        Station 논현역 = new Station("논현역");
        신분당선.addLineStation(논현역, 강남역, 10);

        신분당선.removeLineStation(논현역);

        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }
}
