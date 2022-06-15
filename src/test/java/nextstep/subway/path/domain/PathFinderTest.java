package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.line.domain.LineTest.노선_생성;
import static nextstep.subway.path.application.PathServiceTest.라인_목록_생성;
import static nextstep.subway.station.domain.StationTest.지하철_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PathFinderTest {

    private Station 교대역, 강남역, 역삼역, 선릉역, 한티역, 남부터미널역, 양재역, 매봉역, 도곡역;
    private Line 이호선, 삼호선, 신분당선, 수인분당선;

    /**
     *   교대역--(2호선, 3)--강남역--(2호선, 3)--역삼역--(2호선, 3)--선릉역
     *     |                   |                               (수인분당, 3)
     *  (3호선, 5)          (신분당, 3)                             한티역
     *     |                    |                               (수인분당, 3)
     *  남부터미널역--(3호선, 3)--양재역--(3호선, 3)--매봉역--(3호선, 3)--도곡역
     */

    @BeforeEach
    void setUp() {
        교대역 = 지하철_생성("교대역");
        강남역 = 지하철_생성("강남역");
        역삼역 = 지하철_생성("역삼역");
        선릉역 = 지하철_생성("선릉역");
        한티역 = 지하철_생성("한티역");
        남부터미널역 = 지하철_생성("남부터미널역");
        양재역 = 지하철_생성("양재역");
        매봉역 = 지하철_생성("매봉역");
        도곡역 = 지하철_생성("도곡역");

        이호선 = 노선_생성("이호선", "bg-green-color", 교대역, 강남역, 3);
        이호선.addSection(강남역, 역삼역, 3);
        이호선.addSection(역삼역, 선릉역, 3);

        삼호선 = 노선_생성("삼호선", "bg-orange-color", 교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);
        삼호선.addSection(양재역, 매봉역, 3);
        삼호선.addSection(매봉역, 도곡역, 3);

        신분당선 = 노선_생성("신분당선", "bg-red-color", 강남역, 양재역, 3);

        수인분당선 = 노선_생성("수인분당선", "bg-yellow-color", 선릉역, 한티역, 3);
        수인분당선.addSection(한티역, 도곡역, 3);
    }

    @DisplayName("노선 목록 중 하나의 노선에 속한 2개의 역의 최소 경로를 조회하면 정상 동작해야 한다")
    @Test
    void findShortestPathByOneLine() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(교대역, 선릉역);

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 역삼역, 선릉역);
        최소_노선_길이_일치됨(stations.getDistance(), 9);
    }

    @DisplayName("노선 목록 중 한번의 환승을 통해 도달할 수 있는 최소 경로를 조회하면 정상 동작해야 한다")
    @Test
    void findShortestPathByOneTransfer() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선, 신분당선));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(교대역, 양재역);

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 양재역);
        최소_노선_길이_일치됨(stations.getDistance(), 6);
    }

    @DisplayName("노선 목록 중 도달할 수 있는 여러 경로를 가진 목적지의 경로를 조회하면 최소 경로로 조회해야 한다")
    @Test
    void findShortestPathByMultiplePath() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선, 삼호선, 신분당선, 수인분당선));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(교대역, 도곡역);

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 양재역, 매봉역, 도곡역);
        최소_노선_길이_일치됨(stations.getDistance(), 12);
    }

    @DisplayName("출발역과 도착역이 같은 노선을 조회하면 예외가 발생해야 한다")
    @Test
    void findShortestPathBySameStartAndEndStation() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(교대역, 교대역));
    }

    @DisplayName("도달할 수 없는 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findShortestPathByUnreachablePath() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(신분당선, 수인분당선));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(강남역, 도곡역));
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(한티역, 강남역));
    }

    private void 최소_노선_경로_일치됨(ShortestPathResponse source, Station... target) {
        List<PathStation> stations = source.getStations();

        assertThat(stations.size()).isEqualTo(target.length);

        for (int idx = 0; idx < stations.size(); idx++) {
            assertThat(stations.get(idx).getName()).isEqualTo(target[idx].getName());
        }
    }

    private void 최소_노선_길이_일치됨(int sourceDistance, int targetDistance) {
        assertThat(sourceDistance).isEqualTo(targetDistance);
    }
}
