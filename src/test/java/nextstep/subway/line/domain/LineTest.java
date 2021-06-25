package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

    private Station 강남역, 양재역, 정자역, 광교역;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        정자역 = new Station("정자역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @Test
    @DisplayName("목록 조회")
    void createLine() {
        //when
        List<Station> stations = 신분당선.getStations();

        //then
        assertThat(stations).containsAll(Arrays.asList(강남역, 광교역));
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        신분당선.addStation(강남역, 양재역, 3);

        // then
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 광교역);
    }
    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        신분당선.addStation(강남역, 양재역, 2);
        신분당선.addStation(정자역, 강남역, 5);

        // then
        assertThat(신분당선.getStations()).containsExactly(정자역, 강남역, 양재역, 광교역);
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when, then
        assertThatThrownBy(() -> 신분당선.addStation(강남역, 광교역, 3)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when, then
        assertThatThrownBy(() -> 신분당선.addStation(정자역, 양재역, 3)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        신분당선.addStation(강남역, 양재역, 2);
        신분당선.addStation(양재역, 정자역, 2);

        // when
        신분당선.removeStation(양재역);

        // then
        assertThat(신분당선.getStations()).containsAll(Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        
        // when, then
        assertThatThrownBy(() -> 신분당선.removeStation(강남역)).isInstanceOf(RuntimeException.class);
    }
}