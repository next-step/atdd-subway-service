package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.ShortestPathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Path")
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    @BeforeEach
    void setUp() {
        지하철역_생성();
        노선_생성();
        구간_생성();
        전체_노선 = Arrays.asList(_9호선, _2호선, _3호선, 분당선, 신분당선);
        선정릉역_번호 = 1L;
        양재역_번호 = 2L;
    }

    private Line 신분당선, 분당선, _9호선, _2호선, _3호선;
    private Station 신논현역, 언주역, 선정릉역, 강남역, 역삼역, 선릉역, 양재역, 매봉역, 도곡역, 한티역;
    private Long 선정릉역_번호, 양재역_번호;
    private List<Line> 전체_노선;

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
        // Given
        역_조회_동작_정의(선정릉역_번호, 선정릉역);
        역_조회_동작_정의(양재역_번호, 양재역);
        전체_노선_조회_동작_정의();

        // When
        ShortestPathResponse 최단_경로_응답 = pathService.getShortestPath(선정릉역_번호, 양재역_번호);

        // Then
        verify(stationService, times(2)).findStationById(anyLong());
        verify(lineRepository).findAll();
        List<String> 최단_경로_역_목록 = 최단_경로_응답.getStations()
            .stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
        assertAll(
            () -> assertThat(최단_경로_응답.getDistance()).isEqualTo(32),
            () -> assertThat(최단_경로_응답.getStations())
                .extracting(StationResponse::getName)
                .hasSize(5)
                .containsAnyElementsOf(최단_경로_역_목록)
        );
    }

    @Test
    @DisplayName("출발지와 도착지가 같은 경우 예외")
    public void throwException_WhenSourceIsEqualToTarget() {
        // Given
        역_조회_동작_정의(선정릉역_번호, 선정릉역);
        전체_노선_조회_동작_정의();

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pathService.getShortestPath(선정릉역_번호, 선정릉역_번호));
    }

    @Test
    @DisplayName("출발지와 도착지 사이에 간선이 없는 경우 예외")
    public void throwException_WhenSourceAndTargetIsNotConnected() {
        // Given
        Long 천호역_번호 = 3L;
        Station 천호역 = new Station("천호역");
        Line _8호선 = new Line("9호선", "brown", new Station("강동역"), 천호역, 3);
        전체_노선 = Arrays.asList(_9호선, _2호선, _3호선, 분당선, 신분당선, _8호선);

        역_조회_동작_정의(선정릉역_번호, 선정릉역);
        역_조회_동작_정의(천호역_번호, 천호역);
        전체_노선_조회_동작_정의();

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pathService.getShortestPath(선정릉역_번호, 천호역_번호));
    }

    @Test
    @DisplayName("출발지 혹은 도착지가 존재하지 않는 경우 예외")
    public void throwException_WhenSourceStationOrTargetStationIsNotPersisted() {
        // Given
        given(stationService.findStationById(선정릉역_번호)).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> pathService.getShortestPath(선정릉역_번호, 양재역_번호));
    }

    private void 역_조회_동작_정의(Long 역번호, Station 반환역) {
        given(stationService.findStationById(역번호)).willReturn(반환역);
    }

    private void 전체_노선_조회_동작_정의() {
        given(lineRepository.findAll()).willReturn(전체_노선);
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
