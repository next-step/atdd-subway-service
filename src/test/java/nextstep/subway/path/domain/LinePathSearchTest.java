package nextstep.subway.path.domain;

import static nextstep.subway.station.domain.StationTest.강남역;
import static nextstep.subway.station.domain.StationTest.강변역;
import static nextstep.subway.station.domain.StationTest.개성역;
import static nextstep.subway.station.domain.StationTest.교대역;
import static nextstep.subway.station.domain.StationTest.남부터미널역;
import static nextstep.subway.station.domain.StationTest.분당역;
import static nextstep.subway.station.domain.StationTest.사당역;
import static nextstep.subway.station.domain.StationTest.서울대역;
import static nextstep.subway.station.domain.StationTest.선릉역;
import static nextstep.subway.station.domain.StationTest.시흥대야역;
import static nextstep.subway.station.domain.StationTest.양재역;
import static nextstep.subway.station.domain.StationTest.오리역;
import static nextstep.subway.station.domain.StationTest.은계역;
import static nextstep.subway.station.domain.StationTest.잠실나루역;
import static nextstep.subway.station.domain.StationTest.잠실역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.path.PathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LinePathSearchTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private Line 자바선;
    private Line 호남선;
    private Line 서해선;
    private Line 오호선;
    private Line 육호선;
    private LinePathSearch linePathSearch;
    private LinePathSearch linePathSearch_13세;
    private LinePathSearch linePathSearch_14세;

    /**                           개성역
     *                            |
     *                            *신분당선*(60)
     *                            |
     * 교대역    --- *2호선*(10) ---   강남역  --- *2호선*(10) --- 선릉역--- *2호선*(5) --- 잠실역 --- 5호선(10)(추가요금 900) --- 잠실나루역 --- 6호선(20)(추가요금 1000) --- 강변역
     * |                          |                         |                     |
     * *3호선*(3)                  *신분당선*(10)              *자바선*(1)             *호남선*(10)
     * |                          |                         |                     |
     * 남부터미널역  --- *3호선*(2) --- 양재역 ---  *4호선*(7) --- 오리역 --- *4호선*(10) --- 분당역
     */

    /**
     * 시흥대야역
     * |
     * *서해선*(3)
     * |
     * 은계역
     */
    @BeforeEach
    void setUp() {
        신분당선 = new Line(1L, "신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line(2L, "이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line(3L, "삼호선", "bg-red-600", 교대역, 남부터미널역, 3);
        사호선 = new Line(4L, "사호선", "bg-red-600", 양재역, 오리역, 7);
        자바선 = new Line(5L, "자바선", "bg-red-600", 선릉역, 오리역, 1);
        호남선 = new Line(6L, "호남선", "bg-red-600", 잠실역, 분당역, 10);
        서해선 = new Line(7L, "서해선", "bg-red-600", 시흥대야역, 은계역, 3);
        오호선 = new Line(8L, "오호선", "bg-red-600", 잠실역, 잠실나루역, 10, 900);
        육호선 = new Line(9L, "육호선", "bg-red-600", 잠실나루역, 강변역, 10, 1000);

        이호선.addSection(new Section(이호선, 강남역, 선릉역, 10));
        이호선.addSection(new Section(이호선, 선릉역, 잠실역, 5));

        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 2));
        사호선.addSection(new Section(사호선, 오리역, 분당역, 10));

        신분당선.addSection(new Section(신분당선, 개성역, 강남역, 60));

        List<Section> sectinos = new ArrayList<>();
        sectinos.addAll(이호선.getSections());
        sectinos.addAll(삼호선.getSections());
        sectinos.addAll(신분당선.getSections());
        sectinos.addAll(자바선.getSections());
        sectinos.addAll(호남선.getSections());
        sectinos.addAll(사호선.getSections());
        sectinos.addAll(서해선.getSections());
        sectinos.addAll(오호선.getSections());
        sectinos.addAll(육호선.getSections());

        LoginMember 로그인_13세 = new LoginMember(1L, "7271kim@naver.com", 13);
        LoginMember 로그인_14세 = new LoginMember(2L, "7271kim@naver.com", 14);

        linePathSearch = LinePathSearch.of(sectinos);
        linePathSearch_13세 = LinePathSearch.of(sectinos, 로그인_13세);
        linePathSearch_14세 = LinePathSearch.of(sectinos, 로그인_14세);

    }

    @Test
    @DisplayName("특정 구간의 최단경로와 거리를 반환한다.")
    void searchPathTest() {
        Path path = linePathSearch.searchPath(양재역, 잠실역);
        assertThat(path.getMinDistance()).isEqualTo(13);
        assertThat(path.getStations()).containsExactly(양재역, 오리역, 선릉역, 잠실역);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 Exception을 발생 시킨다.")
    void sameStations() {
        Exception exception = assertThrows(PathException.class, () -> {
            linePathSearch.searchPath(양재역, 양재역);
        });
        assertThat(exception.getMessage()).isEqualTo(PathException.SAME_STATION);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 Exception을 발생 시킨다.")
    void notConnected() {
        Exception exception = assertThrows(PathException.class, () -> {
            linePathSearch.searchPath(교대역, 은계역);
        });
        assertThat(exception.getMessage()).isEqualTo(PathException.NOT_CONNECTED);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 Exception을 발생 시킨다.")
    void noStations() {
        Exception exception = assertThrows(PathException.class, () -> {
            linePathSearch.searchPath(사당역, 서울대역);
        });
        assertThat(exception.getMessage()).isEqualTo(PathException.NO_REGISTRATION);
    }

    @ParameterizedTest
    @DisplayName("구간에 대해 가격을 계산한다.")
    @MethodSource("priceResultSet")
    void calculatorTest(Station source, Station target, int result) {
        assertThat(linePathSearch.searchPath(source, target).getPrice()).isEqualTo(result);
    }

    @Test
    @DisplayName(" 13세 이상~19세 미만은 청소년: 운임에서 350원을 공제한 금액의 20%할인 받는다")
    void discount13() {
        assertThat(linePathSearch_13세.searchPath(개성역, 강남역).getPrice()).isEqualTo(1440);
    }

    @Test
    @DisplayName(" 6세 이상~ 13세 미만은 어린이: 운임에서 350원을 공제한 금액의 50%할인")
    void discount12() {
        assertThat(linePathSearch_13세.searchPath(개성역, 강남역).getPrice()).isEqualTo(900);
    }

    private static Stream<Arguments> priceResultSet() {
        return Stream.of(
            Arguments.of(잠실역, 잠실나루역, 2150),
            Arguments.of(잠실역, 강변역, 2450));
    }

}
