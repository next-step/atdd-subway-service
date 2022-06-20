package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineTest.라인_생성;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private Station 강남역, 양재역, 교대역, 남부터미널역, 서울역, 용산역;
    private Line 신분당선, 이호선, 삼호선, 일호선;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        교대역 = 지하철역_생성("교대역");
        남부터미널역 = 지하철역_생성("남부터미널역");
        서울역 = 지하철역_생성("서울역");
        용산역 = 지하철역_생성("용산역");

        신분당선 = 라인_생성("신분당선", "red", 강남역, 양재역, Distance.of(5));
        이호선 = 라인_생성("이호선", "green", 교대역, 강남역, Distance.of(10));
        삼호선 = 라인_생성("삼호선", "yellow", 교대역, 양재역, Distance.of(5));
        일호선 = 라인_생성("일호선", "blue", 서울역, 용산역, Distance.of(7));

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, Distance.of(3)));

        pathFinder = new PathFinder();
        pathFinder.init(Arrays.asList(신분당선, 이호선, 삼호선, 일호선));
    }

    @Test
    @DisplayName("최단 거리 경로, 거리, 해당 노선 확인")
    void verifyShortestPathAndDistance() {
        Path dijkstraPath = pathFinder.getDijkstraPath(강남역, 남부터미널역);

        assertAll(
                () -> assertThat(dijkstraPath.totalDistance()).isEqualTo(7),
                () -> assertThat(dijkstraPath.throughStations()).containsExactly(강남역, 양재역, 남부터미널역),
                () -> assertThat(dijkstraPath.throughLines()).contains(신분당선, 삼호선)
        );
    }

    @Test
    @DisplayName("경로 조회시 연결되어 있지 않은 역은 에러 발생")
    void searchNotLinkedPath() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathFinder.getDijkstraPath(강남역, 서울역));
    }

    @Test
    @DisplayName("출발, 도착역 동일한 경로 조회시 에러 발생")
    void sameSourceAndTargetPath() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathFinder.getDijkstraPath(강남역, 강남역));
    }
}
