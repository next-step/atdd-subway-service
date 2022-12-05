package nextstep.subway.path.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DijkstraShortestPathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 수인분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 매봉역;
    private Station 양재시민의숲;
    private Station 정자역;

    private List<Line> lines;

    /**
     * 교대역    --- *2호선* ---   강남역  --- *2호선* --- 역삼역   --- *2호선* ---  선릉역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  --- *3호선* ---  매봉
     * *                        |
     * *                        *신분당선*
     * *                        |
     * *                        양재시민의숲
     * <p>
     * 미금 --- *수인분당선* ---  정자
     */

    @BeforeEach
    void setUp() {
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        교대역 = 지하철역_생성("교대역");
        남부터미널역 = 지하철역_생성("남부터미널역");
        역삼역 = 지하철역_생성("역삼역");
        선릉역 = 지하철역_생성("선릉역");
        매봉역 = 지하철역_생성("매봉역");
        양재시민의숲 = 지하철역_생성("양재시민의숲");
        정자역 = 지하철역_생성("정자역");

        신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재시민의숲, 20);
        이호선 = 지하철_노선_생성("이호선", "bg-green-600", 교대역, 선릉역, 30);
        삼호선 = 지하철_노선_생성("삼호선", "bg-orange-600", 교대역, 매봉역, 30);
        수인분당선 = 지하철_노선_생성("수인분당선", "bg-yello-600", 교대역, 매봉역, 30);

        지하철_노선에_지하철역_등록(삼호선, 교대역, 남부터미널역, 10);
        지하철_노선에_지하철역_등록(삼호선, 남부터미널역, 양재역, 10);
        지하철_노선에_지하철역_등록(이호선, 교대역, 강남역, 10);
        지하철_노선에_지하철역_등록(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록(신분당선, 강남역, 양재역, 10);

        lines = Arrays.asList(신분당선, 이호선, 삼호선, 수인분당선);
    }


    @Test
    @DisplayName("다익스트라 길찾기 도메인 생성")
    void create() {
        // given
        DijkstraShortestPathFinder dijkstraShortestPathFinder = DijkstraShortestPathFinder.from(lines);

        // expect
        assertThat(dijkstraShortestPathFinder).isNotNull();
    }

    @Test
    @DisplayName("출발역과 도착역을 받아 최단경로 조회")
    void find() {
        // given
        DijkstraShortestPathFinder dijkstraShortestPathFinder = DijkstraShortestPathFinder.from(lines);

        // when
        Path path = dijkstraShortestPathFinder.findPath(강남역, 매봉역);

        // then
        assertThat(path.getStations()).containsExactly(강남역, 양재역, 매봉역);
        assertThat(path.getDistance()).isEqualTo(20);
    }


    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    private void 지하철_노선에_지하철역_등록(Line line, Station upStation, Station downStation, int distance) {
        line.addSection(new Section.Builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build());
    }
}
