package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class LineTest {

    @DisplayName("지하철 노선에 구간을 추가한다. (중간역)")
    @Test
    void addLineStationWithMiddleStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");

        //when
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(강남역, 양재역, 6);

        //then
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(강남역, 양재역, 교대역);
    }

    @DisplayName("지하철 노선에 구간을 추가한다. (상행역)")
    @Test
    void addLineStationWithUpStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");

        //when
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(양재역, 강남역,6);

        //then
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(양재역, 강남역, 교대역);
    }

    @DisplayName("지하철 노선에 구간을 추가한다. (하행역)")
    @Test
    void addLineStationWithDownStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");

        //when
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(교대역, 양재역, 6);

        //then
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(강남역, 교대역, 양재역);
    }
    
    @DisplayName("지하철 노선이 구간 추가 시 이미 추가된 구간일 때")
    @Test
    void AlreadyStationIfAddLineStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");

        //when
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);

        assertThatThrownBy(() -> line.addLineStation(강남역, 교대역, 6)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선이 구간 추가 시 상행과 하행이 존재하지 않을 때")
    @Test
    void NotContainsStationIfAddLineStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");

        //when
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);

        assertThatThrownBy(() -> line.addLineStation(양재역, 광교역, 6)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선의 모든 지하철을 상행부터 하행 순으로 조회한다.")
    @Test
    void getStationsTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(강남역, 양재역, 5);

        //when
        List<Station> stations = line.getStations();

        //then
        assertThat(stations).contains(강남역, 교대역, 양재역);
    }

    @DisplayName("지하철 노선에 구간을 제외한다. (중간역)")
    @Test
    void removeLineMiddleStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(양재역, 강남역, 5);

        //when
        line.removeSection(강남역);

        //then
        assertThat(line.getStations().size()).isEqualTo(2);
        assertThat(line.getStations()).containsExactly(양재역, 교대역);
        assertThat(line.getSections().stream()
                .map(Section::getDistance)
                .mapToInt(Distance::getValue)
                .sum()).isEqualTo(15);
    }

    @DisplayName("지하철 노선에 구간을 제외한다. (상행역)")
    @Test
    void removeLineUpStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(양재역, 강남역, 5);

        //when
        line.removeSection(양재역);

        //then
        assertThat(line.getStations().size()).isEqualTo(2);
        assertThat(line.getStations()).containsExactly(강남역, 교대역);
        assertThat(line.getSections().stream()
                .map(Section::getDistance)
                .mapToInt(Distance::getValue)
                .sum())
                .isEqualTo(10);
    }


    @DisplayName("지하철 노선에 구간을 제외한다. (하행역)")
    @Test
    void removeLineDownStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);
        line.addLineStation(양재역, 강남역, 5);

        //when
        line.removeSection(교대역);

        //then
        assertThat(line.getStations().size()).isEqualTo(2);
        assertThat(line.getStations()).containsExactly(양재역, 강남역);
        assertThat(line.getSections().stream()
                .map(Section::getDistance)
                .mapToInt(Distance::getValue)
                .sum())
                .isEqualTo(5);
    }

    @DisplayName("지하철 노선에 구간이 1개 이하일 때 구간을 제외한다.")
    @Test
    void OneSectionIfRemoveLineStationTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line line = new Line("신분당선", "red", 강남역, 교대역, 10);

        //when
        assertThatThrownBy(() -> line.removeSection(강남역)).isInstanceOf(RuntimeException.class);
    }
}
