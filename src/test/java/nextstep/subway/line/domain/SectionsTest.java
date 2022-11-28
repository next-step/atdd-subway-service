package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    Station 강남역;
    Station 광교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
    }

    @DisplayName("Sections 등록된 노선이 없으면 조회시 빈 리스트가 반환된다.")
    @Test
    void empty_stations() {
        List<Station> stations = new Sections().extractStations();

        assertThat(stations).isEmpty();
    }

    @DisplayName("등록된 역을 조회할 수 있다.")
    @Test
    void get_stations() {
        Sections sections = new Sections();
        sections.addLineStation(null, 강남역, 광교역, 10);

        assertThat(sections.extractStations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("역 사이에 새로운 역을 추가할 수 있다.")
    @Test
    void add_new_station() {
        Sections sections = new Sections();
        sections.addLineStation(null, 강남역, 광교역, 10);
        Station 양재역 = new Station("양재역");

        sections.addLineStation(null, 강남역, 양재역, 5);

        assertThat(sections.extractStations()).containsExactly(강남역, 양재역, 광교역);

    }

    @DisplayName("새로운 역을 상행 종점으로 등록할 수 있다.")
    @Test
    void add_up_station() {
        Sections sections = new Sections();
        sections.addLineStation(null, 강남역, 광교역, 10);
        Station 논현역 = new Station("논현역");

        sections.addLineStation(null, 논현역, 강남역, 10);

        assertThat(sections.extractStations()).containsExactly(논현역, 강남역, 광교역);
    }


    @DisplayName("새로운 역을 상행 종점으로 등록할 수 있다.")
    @Test
    void add_down_station() {
        Sections sections = new Sections();
        sections.addLineStation(null, 강남역, 광교역, 10);
        Station 호매실역 = new Station("호매실역");

        sections.addLineStation(null, 광교역, 호매실역, 10);

        assertThat(sections.extractStations()).containsExactly(강남역, 광교역, 호매실역);
    }

    @DisplayName("구간이 하나인 노선에서 마지막 구간을 제거할때 RuntimeException 이 발생한다.")
    @Test
    void remove_last_one_section() {
        Sections sections = new Sections();
        sections.addLineStation(null, 강남역, 광교역, 10);

        assertThatThrownBy(() -> sections.removeLineStation(null, 강남역)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("노선에서 역을 제거할 수 있다.")
    @Test
    void remove_station() {
        Sections sections = new Sections();
        Station 논현역 = new Station("논현역");
        sections.addLineStation(null, 강남역, 광교역, 10);
        sections.addLineStation(null, 논현역, 강남역, 5);

        sections.removeLineStation(null, 논현역);

        assertThat(sections.extractStations()).containsExactly(강남역, 광교역);
    }

}
