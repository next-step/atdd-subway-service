package nextstep.subway.path.domain;

import static nextstep.subway.station.domain.StationTest.강남역;
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
import static nextstep.subway.station.domain.StationTest.잠실역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.exception.PathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class LinePathSearchTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private Line 자바선;
    private Line 호남선;
    private Line 서해선;
    private LinePathSearch linePathSearch;

    /**
     * 교대역    --- *2호선*(10) ---   강남역  --- *2호선*(10) --- 선릉역--- *2호선*(5) --- 잠실역
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
        삼호선 = new Line(3L, "삼호선", "bg-red-600", 교대역, 양재역, 5);
        사호선 = new Line(4L, "사호선", "bg-red-600", 양재역, 오리역, 7);
        자바선 = new Line(5L, "자바선", "bg-red-600", 선릉역, 오리역, 1);
        호남선 = new Line(6L, "호남선", "bg-red-600", 잠실역, 분당역, 10);
        서해선 = new Line(7L, "서해선", "bg-red-600", 시흥대야역, 은계역, 3);

        이호선.addSection(new Section(이호선, 강남역, 선릉역, 10));
        이호선.addSection(new Section(이호선, 선릉역, 잠실역, 5));

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
        사호선.addSection(new Section(사호선, 오리역, 분당역, 10));
        linePathSearch = LinePathSearch.of(Arrays.asList(이호선, 삼호선, 신분당선, 자바선, 호남선, 사호선, 서해선));

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
        assertThat(exception.getMessage()).isEqualTo("검색하려는 두 역이 동일합니다. 다른 역을 입력하세요");
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 Exception을 발생 시킨다.")
    void notConnected() {
        Exception exception = assertThrows(PathException.class, () -> {
            linePathSearch.searchPath(교대역, 은계역);
        });
        assertThat(exception.getMessage()).isEqualTo("출발역과 도착역이 연결되지 않았습니다.");
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 Exception을 발생 시킨다.")
    void noStations() {
        Exception exception = assertThrows(PathException.class, () -> {
            linePathSearch.searchPath(사당역, 서울대역);
        });
        assertThat(exception.getMessage()).isEqualTo("검색하려는 역이 라인에 등록되어 있지 않습니다.");
    }

}
