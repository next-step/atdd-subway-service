package nextstep.subway.line;

import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import nextstep.subway.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;
import nextstep.subway.station.dto.*;

class LineTest extends AcceptanceTest {
    @DisplayName("디폴트 생성자를 호출하면 Line 객체를 반환한다.")
    @Test
    void constructTest1() {
        assertThat(new Line()).isInstanceOf(Line.class);
    }

    @DisplayName("이름, 색을 인자로 하는 생성자를 호출하면 Line 객체를 반환한다.")
    @Test
    void constructTest2() {
        assertThat(new Line("신분당선", "bg-red-600")).isInstanceOf(Line.class);
    }

    @DisplayName("이름, 색, 상행역, 하행역, 거리를 인자로 하는 생성자를 호출하면 Line 객체를 반환한다.")
    @Test
    void constructTest3() {
        assertThat(
            new Line("신분당선", "bg-red-600", Station.from("강남역"), Station.from("광교역"), Distance.from(10))).isInstanceOf(
            Line.class);
    }

    @DisplayName("정적 팩토리 메서드 of로 이름, 색, 상행역, 하행역, 거리를 입력받으면 Line 객체를 반환한다.")
    @Test
    void staticFactoryMethodTest1() {
        assertThat(
            Line.of("신분당선", "bg-red-600", Station.from("강남역"), Station.from("광교역"), Distance.from(10))).isInstanceOf(
            Line.class);
    }

    @DisplayName("정적 팩토리 메서드 from으로 LineRequest를 입력받으면 Line 객체를 반환한다.")
    @Test
    void staticFactoryMethodTest2() {
        StationResponse 강남역응답 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 광교역응답 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        assertThat(Line.from(new LineRequest("신분당선", "bg-red-600", 강남역응답.getId(), 광교역응답.getId(), 10))).isInstanceOf(
            Line.class);
    }

    @DisplayName("Line객체를 입력받는 update함수를 호출하면 해당 객체의 속성인 노선이름과 노선색이 변경된다.")
    @Test
    void updateTest() {
        Line 팔호선 = new Line("팔호선", "pink");
        Line 이호선 = new Line("이호선", "green");
        팔호선.update(이호선);
        assertThat(팔호선.getName()).isEqualTo(이호선.getName());
        assertThat(팔호선.getColor()).isEqualTo(이호선.getColor());
    }

    @DisplayName("등록한 역이 있고 getStations를 호출하면, 역목록을 얻을 수 있다.")
    @Test
    void getStationsTest() {
        Station 강남역 = Station.from("강남역");
        Station 광교역 = Station.from("광교역");
        Line line = Line.of("신분당선", "bg-red-600", 강남역, 광교역, Distance.from(10));
        assertThat(line.stations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("등록한 역이 있고, removeLineStation을 호출하여 역을 제거할 수 있다.")
    @Test
    void removeLineStationTest() {
        Station 강남역 = Station.from("강남역");
        Station 양재역 = Station.from("양재역");
        Station 광교역 = Station.from("광교역");
        Line line = Line.of("신분당선", "bg-red-600", 강남역, 양재역, Distance.from(10));
        line.addSection(양재역, 광교역, Distance.from(20));

        line.removeLineStation(양재역);
        assertThat(line.stations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("등록한 구간이 한 개 존재하고, removeLineStation을 호출하여 예외를 던진다.")
    @Test
    void exceptionTest() {
        Station 강남역 = Station.from("강남역");
        Station 광교역 = Station.from("광교역");
        Line line = Line.of("신분당선", "bg-red-600", 강남역, 광교역, Distance.from(10));

        assertThatThrownBy(() -> line.removeLineStation(강남역)).isInstanceOf(RuntimeException.class);
    }
}
