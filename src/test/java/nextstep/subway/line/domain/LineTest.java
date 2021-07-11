package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.domain.Line.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {

    private Station 강남역;
    private Station 광교역;
    private Station 정자역;
    private Station 양재역;
    private Line 신분당선;

    @BeforeEach
    public void setup() {
        강남역 = new Station("강남역");
        정자역 = new Station("정자역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 30, 900);
    }

    @DisplayName("라인 생성")
    @Test
    public void createLine() {
        //given
        Station 서울역 = new Station("서울역");
        Station 안산역 = new Station("안산역");

        //when
        Line 사호선 = new Line("사호선", "bg-blue-600", 서울역, 안산역, 30);

        //then
        assertThat(사호선.extraCharge()).isZero();
    }

    @DisplayName("라인의 상행 종점 역 찾기")
    @Test
    public void findUpStation() {
        //when
        Station findStation = 신분당선.findUpStation();

        //then
        assertThat(findStation.equals(강남역)).isTrue();
    }

    @DisplayName("라인의 모든 역 상행선 -> 하행선 순서로 가져오기")
    @Test
    public void findLineAllStations() {
        //when
        List<Station> stations = 신분당선.stations();

        //then
        assertThat(stations.get(0).equals(강남역)).isTrue();
        assertThat(stations.get(1).equals(광교역)).isTrue();
    }

    @DisplayName("역 추가 실패 - 이미 존재하는 구간 추가")
    @Test
    public void alreadyExistsStationAdd() {
        assertThatThrownBy(() -> 신분당선.addStation(강남역, 광교역, 50)).isInstanceOf(RuntimeException.class).hasMessageContaining(ALREADY_EXISTS_SECTION);
    }

    @DisplayName("역 추가 실패 - 기존에 존재하지 않는 구간 추가")
    @Test
    public void notExistsStationsAdd() {
        assertThatThrownBy(() -> 신분당선.addStation(양재역, 정자역, 10)).isInstanceOf(RuntimeException.class).hasMessageContaining(NOT_ADDED_SECTION);
    }

    @DisplayName("상행역 기준으로 역 추가")
    @Test
    public void addStationByUpStation() {
        //when
        신분당선.addStation(강남역, 양재역, 100);

        //then
        List<Station> stations = 신분당선.stations();
        assertThat(stations.get(0).equals(강남역)).isTrue();
        assertThat(stations.get(1).equals(양재역)).isTrue();
    }

    @DisplayName("하행역 기준으로 역 추가")
    @Test
    public void addStationByDownStation() {
        //when
        신분당선.addStation(양재역, 광교역, 10);

        //then
        List<Station> stations = 신분당선.stations();
        assertThat(stations.get(0).equals(강남역)).isTrue();
        assertThat(stations.get(1).equals(양재역)).isTrue();
        assertThat(stations.get(2).equals(광교역)).isTrue();
    }

    @DisplayName("역 삭제 실패 - 존재하지 않는 구간 삭제")
    @Test
    public void emptyStationDelete() {
        assertThatThrownBy(() -> 신분당선.removeStation(강남역)).isInstanceOf(RuntimeException.class).hasMessageContaining(DELETE_FAIL_SECTION);
    }

    @DisplayName("상행선 종점 역 삭제")
    @Test
    public void removeUpStation() {
        //given
        신분당선.addStation(강남역, 양재역, 10);

        //when
        신분당선.removeStation(강남역);

        //then
        List<Station> stations = 신분당선.stations();
        assertThat(stations.get(0).equals(양재역)).isTrue();
        assertThat(stations.get(1).equals(광교역)).isTrue();
    }

    @DisplayName("구간 사이의 역 삭제")
    @Test
    public void removeBetweenStation() {
        //given
        신분당선.addStation(강남역, 양재역, 10);

        //when
        신분당선.removeStation(양재역);

        //then
        List<Station> stations = 신분당선.stations();
        assertThat(stations.get(0).equals(강남역)).isTrue();
        assertThat(stations.get(1).equals(광교역)).isTrue();
    }

    @DisplayName("하행선 종점 역 삭제")
    @Test
    public void removeDownStation() {
        //given
        신분당선.addStation(강남역, 양재역, 10);

        //when
        신분당선.removeStation(광교역);

        //then
        List<Station> stations = 신분당선.stations();
        assertThat(stations.get(0).equals(강남역)).isTrue();
        assertThat(stations.get(1).equals(양재역)).isTrue();
    }

}
