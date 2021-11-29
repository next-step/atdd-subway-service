package nextstep.subway.path.finder;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

class PathFinderTest {
    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_5 = 5;
    private static final int DISTANCE_3 = 3;
    private static final int DISTANCE_0 = 0;
    private static final int FARE_1000 = 1000;
    private static final int FARE_900 = 900;
    private static final int FARE_800 = 800;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void beforeEach() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");

        신분당선 = Line.of("신분당선", "RED", FARE_1000, 강남역, 양재역, DISTANCE_10);
        이호선 = Line.of("이호선", "GREED", FARE_900, 교대역, 강남역, DISTANCE_10);
        삼호선 = Line.of("삼호선", "ORANGE", FARE_800, 남부터미널역, 양재역, DISTANCE_5);
        삼호선.addSection(Section.of(교대역, 남부터미널역, Distance.from(DISTANCE_3)));
    }

    @DisplayName("노선이 없으면 최단경로 조회 시, 예외가 발생한다.")
    @Test
    void findShortestPath1() {
        // when
        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Collections.emptyList()));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(양재역, 강남역));
    }

    @DisplayName("출발역에서 도착역까지 최단경로를 조회한다.")
    @Test
    void findShortestPath2() {
        // given
        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when
        ShortestPath shortestPath = pathFinder.findShortestPath(교대역, 양재역);

        // then
        List<Long> expectedIds = StreamUtils.mapToList(Arrays.asList(교대역, 남부터미널역, 양재역), Station::getId);
        List<Long> actualIds = StreamUtils.mapToList(shortestPath.getStations(), StationResponse::getId);

        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
        assertThat(shortestPath.getDistance()).isEqualTo(DISTANCE_3 + DISTANCE_5);
    }

    @DisplayName("출발역과 도착역이 같을 때 최단경로는 출발역 1개, 최단거리는 0 이다.")
    @Test
    void findShortestPath3() {
        // given
        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when
        ShortestPath shortestPath = pathFinder.findShortestPath(교대역, 교대역);

        // then
        List<Long> actualIds = StreamUtils.mapToList(shortestPath.getStations(), StationResponse::getId);

        assertThat(actualIds).containsExactly(교대역.getId());
        assertThat(shortestPath.getDistance()).isEqualTo(DISTANCE_0);
    }

    @DisplayName("출발역이 노선상에 존재하지 않을때 예외가 발생한다.")
    @Test
    void findShortestPath4() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(서울역, 강남역));
    }

    @DisplayName("도착역이 노선상에 존재하지 않을때 예외가 발생한다.")
    @Test
    void findShortestPath5() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(양재역, 서울역));
    }

    @DisplayName("출발역과 도착역이 서로 연결되어 있지 않을때 예외가 발생한다.")
    @Test
    void findShortestPath6() {
        // given
        Station 서울역 = Station.of(5L, "서울역");
        Station 시청역 = Station.of(6L, "시청역");
        Line 일호선 = Line.of("일호선", "BLUE", FARE_1000, 서울역, 시청역, DISTANCE_5);

        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Arrays.asList(신분당선, 일호선, 이호선, 삼호선)));

        // when & then
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> pathFinder.findShortestPath(강남역, 서울역));
    }
}