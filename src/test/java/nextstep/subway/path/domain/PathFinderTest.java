package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationSimpleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Line")
class PathFinderTest {
    private Line 신분당선, 분당선, _9호선, _2호선, _3호선;
    private Station 신논현역, 언주역, 선정릉역, 강남역, 역삼역, 선릉역, 양재역, 매봉역, 도곡역, 한티역;
    private List<Line> 전체_노선;

    @BeforeEach
    void setUp() {
        지하철역_생성();
        노선_생성();
        구간_생성();
        전체_노선 = Arrays.asList(_9호선, _2호선, _3호선, 분당선, 신분당선);
    }

    /**
     *  [신분당선]                           [분당선]
     *   |                                      |
     *   |                                     |
     * 신논현 -  (7)  - 언주  -   (18)   -  `선정릉`  ---> [9호선]
     *   |                                    |
     *  (5)                                 (4)
     *   |                                  |
     * 강남   -    (6)   -   역삼 - (1) - 선릉  ---> [2호선]
     *   |                                |
     *   |                              (9)
     *   |                              |
     *  (12)                          한티
     *   |                             |
     *   |                            (6)
     *   |                            |
     * `양재` - (1) - 매봉 - (0.8) - 도곡  ---> [3호선]
     *
     * path 1: 선정릉 -> 언주 -> 신논현 -> 강남 -> 양재 : 4개역, 42
     * path 2: 선정릉 -> 선릉 -> 한티 -> 도곡 -> 매봉 -> 양재 : 5개역, 37
     * path 3: 선정릉 -> 선릉 -> 역삼 -> 강남 -> 양재 : 4개역, 32
     */
    @Test
    @DisplayName("전체 노선에서 출발지로부터 도착지까지의 최단 경로 조회")
    public void findShortestPath() {
        // When
        PathFinder pathFinder = new PathFinder();
        ShortestPathResponse 최단_경로_응답 = pathFinder.findShortestPath(전체_노선, 선정릉역, 양재역);

        // Then
        List<String> 최단_경로_역_목록 = 최단_경로_응답.getStations()
            .stream()
            .map(StationSimpleResponse::getName)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(최단_경로_응답.getDistance()).isEqualTo(32),
            () -> assertThat(최단_경로_응답.getStations())
                .extracting(StationSimpleResponse::getName)
                .hasSize(5)
                .containsAnyElementsOf(최단_경로_역_목록)
        );
    }

    @Test
    @DisplayName("출발지와 도착지가 같은 경우 예외 발생 검증")
    public void throwException_WhenSourceIsEqualToTarget() {
        // Given
        PathFinder pathFinder = new PathFinder();

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pathFinder.findShortestPath(전체_노선, 선정릉역, 선정릉역));
    }

    @Test
    @DisplayName("출발지 혹은 도착지가 전체 노선에 포함된 역이 아닌 경우")
    public void throwException_WhenSourceAndTargetIsNotContainsInLines() {
        // Given
        PathFinder pathFinder = new PathFinder();
        Station 천호역 = new Station("천호역");

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pathFinder.findShortestPath(전체_노선, 선정릉역, 천호역));
    }

    @Test
    @DisplayName("출발지와 도착지 사이에 간선이 없는 경우 예외 발생 검증")
    public void throwException_WhenSourceAndTargetIsNotConnected() {
        // Given
        PathFinder pathFinder = new PathFinder();
        Station 천호역 = new Station("천호역");
        Line _8호선 = new Line("9호선", "brown", new Station("강동역"), 천호역, 3);
        List<Line> 전체_노선 = Arrays.asList(_9호선, _2호선, _3호선, 분당선, 신분당선, _8호선);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pathFinder.findShortestPath(전체_노선, 선정릉역, 천호역));
    }

    public void 지하철역_생성() {
        신논현역 = new Station("신논현역");
        언주역 = new Station("언주역");
        선정릉역 = new Station("선정릉역");

        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");

        양재역 = new Station("양재역");
        매봉역 = new Station("매봉역");
        도곡역 = new Station("도곡역");

        한티역 = new Station("한티역");
    }

    public void 노선_생성() {
        _9호선 = new Line("9호선", "brown", 신논현역, 선정릉역, 25);
        _2호선 = new Line("2호선", "green", 강남역, 선릉역, 16);
        _3호선 = new Line("3호선", "orange", 양재역, 도곡역, 18);
        분당선 = new Line("분당선", "yellow", 선정릉역, 도곡역, 19);
        신분당선 = new Line("신분당선", "red", 신논현역, 양재역, 17);
    }

    public void 구간_생성() {
        구간_추가(_9호선, 신논현역, 언주역, 7);
        구간_추가(_2호선, 강남역, 역삼역, 6);
        구간_추가(_3호선, 양재역, 매봉역, 10);
        구간_추가(신분당선, 신논현역, 강남역, 5);
        구간_추가(분당선, 선정릉역, 선릉역, 4);
        구간_추가(분당선, 선릉역, 한티역, 9);
    }

    private void 구간_추가(final Line 노선, final Station 상행역, final Station 하행역, final int 구간_거리) {
        노선.addSection(Section.of(노선, 상행역, 하행역, 구간_거리));
    }
}
