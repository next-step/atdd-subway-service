package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathFinderTest {

    private Station 강남역 = new Station("강남역");
    private Station 판교역 = new Station("판교역");
    private Station 수지역 = new Station("수지역");
    private Station 광교역 = new Station("광교역");
    private Station 홍대역 = new Station("홍대역");

    private Line 신분당선_강남_판교_수지_광교 = new Line("신분당선", "red", 강남역, 광교역, 20);

    @BeforeEach
    void set_신분당선_강남_판교_수지_광교() {
        신분당선_강남_판교_수지_광교.addSection(강남역, 판교역, 4);
        신분당선_강남_판교_수지_광교.addSection(판교역, 수지역, 7);
    }

    @Test
    @DisplayName("지하철 노선에 미등록된 역 조회 (강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_노선에_미등록_역_조회_요청() {
        PathFinder pathFinder = new PathFinder(신분당선_강남_판교_수지_광교);

        assertThatThrownBy(() -> {
            pathFinder.getDijkstraShortestPath(강남역, 홍대역);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("지하철 노선 역 조회 출발지와 도착지 동일 오류 (강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_노선에_조회_요청_출발역_도착역_동일_오류() {
        PathFinder pathFinder = new PathFinder(신분당선_강남_판교_수지_광교);

        assertThatThrownBy(() -> {
            pathFinder.getDijkstraShortestPath(강남역, 강남역);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("지하철 노선에 등록된 역 기준으로 순방향 조회 ([ 강남 --(4)> 판교 --(7)> 수지 ] --(9)> 광교)")
    public void 지하철_노선에_등록_역_조회_요청() {
        PathFinder pathFinder = new PathFinder(신분당선_강남_판교_수지_광교);

        List<Station> actual = pathFinder.getDijkstraShortestPath(강남역, 수지역);
        int actualDistance = pathFinder.getSumLineStationsDistance(강남역, 수지역);

        assertAll(
                () -> assertThat(actual).containsExactly(강남역, 판교역, 수지역),
                () -> assertThat(actualDistance).isEqualTo(11)
        );

    }

    @Test
    @DisplayName("지하철 노선에 등록된 역 기준으로 역방향 조회 (강남 <(4)-- [ 판교 <(7)-- 수지 <(9)-- 광교 ] )")
    public void 지하철_노선에_등록_역_조회_요청_역방향() {
        PathFinder pathFinder = new PathFinder(신분당선_강남_판교_수지_광교);

        List<Station> actual = pathFinder.getDijkstraShortestPath(광교역, 판교역);
        int actualDistance = pathFinder.getSumLineStationsDistance(광교역, 판교역);

        assertAll(
                () -> assertThat(actual).containsExactly(광교역, 수지역, 판교역),
                () -> assertThat(actualDistance).isEqualTo(16)
        );
    }

}