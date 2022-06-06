package nextstep.subway.path.finder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");

        신분당선 = Line.of("신분당선", "RED", 강남역, 양재역, 10);
        이호선 = Line.of("이호선", "GREED", 교대역, 강남역, 10);
        삼호선 = Line.of("삼호선", "ORANGE", 남부터미널역, 양재역, 5);
        삼호선.addSection(Section.of(교대역, 남부터미널역, 3));
    }

    @DisplayName("출발역에서 도착역까지 최단경로를 조회한다.")
    @Test
    void find01() {

        // given
        PathFinder dijkstraPathFinder = DijkstraPathFinder.from(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when
        PathResponse pathResponse = dijkstraPathFinder.findShortPath(교대역, 양재역);

        // then
        List<Station> expectedStations = Lists.newArrayList(교대역, 남부터미널역, 양재역);
        List<Long> expectedStationIds = StreamUtils.mapToList(expectedStations, Station::getId);
        List<Long> actualStationIds = StreamUtils.mapToList(pathResponse.getStations(), StationResponse::getId);

        assertAll(
                () -> assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds),
                () -> assertThat(Distance.from(pathResponse.getDistance())).isEqualTo(Distance.from(8))
        );
    }

    @DisplayName("노선이 없는 경우 최단거리 조회 시 예외가 발생한다.")
    @Test
    void exception01() {
        // given
        PathFinder pathFinder = DijkstraPathFinder.from(Lists.newArrayList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortPath(교대역, 양재역));
    }

    @DisplayName("노선에 존재하지 않는 역을 출발역으로 조회하는 경우 예외가 발생한다.")
    @Test
    void exception02() {
        // given
        Station 수서역 = Station.of(5L, "수서역");
        PathFinder pathFinder = DijkstraPathFinder.from(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortPath(수서역, 교대역));
    }

    @DisplayName("노선에 존재하지 않는 역을 도착역으로 조회하는 경우 예외가 발생한다.")
    @Test
    void exception03() {
        // given
        Station 수서역 = Station.of(5L, "수서역");
        PathFinder pathFinder = DijkstraPathFinder.from(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortPath(교대역, 수서역));
    }

    @DisplayName("노선에 존재하는 역을 출발역, 도착역으로 동일하게 조회하는 경우 결과는 1개의 역과, 거리는 0으로 조회된다.")
    @Test
    void find02() {
        // given
        PathFinder pathFinder = DijkstraPathFinder.from(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when
        PathResponse pathResponse = pathFinder.findShortPath(교대역, 교대역);

        // then
        List<StationResponse> expectedStations = Lists.newArrayList(StationResponse.of(교대역));
        List<Long> expectedStationIds = StreamUtils.mapToList(expectedStations, StationResponse::getId);
        List<Long> actualStationIds = StreamUtils.mapToList(pathResponse.getStations(), StationResponse::getId);
        assertAll(
                () -> assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds),
                () -> assertThat(pathResponse.getDistance()).isZero()
        );
    }
}