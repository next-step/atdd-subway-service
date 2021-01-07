package nextstep.subway.line.domain;

import nextstep.subway.line.exception.AlreadyExistSectionException;
import nextstep.subway.line.exception.NoMatchStationsException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 잠실역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
        잠실역 = new Station(3L, "잠실역");

        신분당선 = new Line(1L, "신분당선", "bg-red-600", 강남역, 역삼역, 10);
    }

    @DisplayName("지하철 노선에 속한 역들 조회(순서별)")
    @Test
    void getStations() {
        //given
        //when
        List<Station> stations = 신분당선.getStations();
        //then
        assertThat(stations).containsExactly(강남역, 역삼역);
    }

    @DisplayName("노선에 역 추가 - 기존 upStation 기준 중간에 추가")
    @Test
    void addLineStation1() {
        //given
        //when
        신분당선.addLineStation(강남역, 잠실역, 4);

        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 잠실역, 역삼역);
    }

    @DisplayName("노선에 역 추가 - 기존 downStation 기준 중간에 추가")
    @Test
    void addLineStation2() {
        //given
        //when
        신분당선.addLineStation(잠실역, 역삼역, 4);

        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 잠실역, 역삼역);
    }

    @DisplayName("노선에 역 추가 - 최 상행역 앞에 추가")
    @Test
    void addLineStation3() {
        //given
        //when
        신분당선.addLineStation(잠실역, 강남역, 4);

        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(잠실역, 강남역, 역삼역);
    }

    @DisplayName("노선에 역 추가 - 최 하행역 뒤에 추가")
    @Test
    void addLineStation4() {
        //given
        //when
        신분당선.addLineStation(역삼역, 잠실역, 4);

        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 역삼역, 잠실역);
    }

    @DisplayName("노선에 역 추가 - 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        //given
        신분당선.addLineStation(역삼역, 잠실역, 4);

        //when
        //then
        assertThatThrownBy(() -> {
            신분당선.addLineStation(역삼역, 잠실역, 4);
        }).isInstanceOf(AlreadyExistSectionException.class);
    }

    @DisplayName("노선에 역 추가 - 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        //when
        //then
        Station 선릉역 = new Station(4L, "선릉역");
        assertThatThrownBy(() -> {
            신분당선.addLineStation(잠실역, 선릉역, 4);
        }).isInstanceOf(NoMatchStationsException.class);
    }

    @DisplayName("노선에 역 삭제 - 최 상행역 삭제 ")
    @Test
    void removeLineStation1() {
        //given
        신분당선.addLineStation(강남역, 잠실역, 4);

        //when
        신분당선.removeLineStation(강남역);
        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(잠실역, 역삼역);
    }

    @DisplayName("노선에 역 삭제 - 중간역 삭제")
    @Test
    void removeLineStation2() {
        //given
        신분당선.addLineStation(강남역, 잠실역, 4);

        //when
        신분당선.removeLineStation(잠실역);
        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 역삼역);
    }

    @DisplayName("노선에 역 삭제 - 최 하행역 삭제")
    @Test
    void removeLineStation3() {
        //given
        신분당선.addLineStation(강남역, 잠실역, 4);

        //when
        신분당선.removeLineStation(역삼역);
        //then
        List<Station> stations = 신분당선.getStations();
        assertThat(stations).containsExactly(강남역, 잠실역);
    }

    @DisplayName("노선에 역 삭제 - 구간이 하나인 노선에서 마지막 구간을 제거할 때")
    @Test
    void removeLineSectionWhenTwoStationRemain() {
        //given
        //when
        assertThatThrownBy(() -> {
            신분당선.removeLineStation(강남역);
        }).isInstanceOf(CannotRemoveSectionException.class);
    }
}