package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신분당선 = Line.builder()
                .name("신분당선")
                .color("red")
                .upStation(강남역)
                .downStation(양재역)
                .distance(20)
                .addedFare(150)
                .build();
        이호선 = Line.builder()
                .name("이호선")
                .color("green")
                .upStation(강남역)
                .downStation(교대역)
                .distance(15)
                .addedFare(300)
                .build();
        삼호선 = Line.builder()
                .name("삼호선")
                .color("yellow")
                .upStation(교대역)
                .downStation(남부터미널역)
                .distance(20)
                .addedFare(500)
                .build();
    }

    @DisplayName("교대역 -> 강남역 -> 양재역 (20 + 15) 최단거리 테스트")
    @Test
    void findShortestPath() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse pathResponse = pathService.findShortestPath(source, target, null);

        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(35),
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactlyElementsOf(Arrays.asList("교대역", "강남역", "양재역"))
        );
    }

    @DisplayName("존재하지 않는 지하철역으로 지하철 경로 조회를 하면 경로를 조회할 수 없다.")
    @Test
    void findShortestPath_exception1() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(null);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathService.findShortestPath(source, target, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("연결되지 않은 출발역과 도착역 사이의 경로를 조회할 수 없다.")
    @Test
    void findShortestPath_exception2() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(남부터미널역);
        when(lineRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> pathService.findShortestPath(source, target, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 같으면 경로를 조회할 수 없다.")
    @Test
    void findShortestPath_exception3() {
        Long source = 1L;
        Long target = 1L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(교대역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 삼호선));

        assertThatThrownBy(() -> pathService.findShortestPath(source, target, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("교대역 -> 강남역 -> 양재역 요금을 조회한다. (나이 없음)")
    @Test
    void 요금_조회() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse pathResponse = pathService.findShortestPath(source, target, null);

        assertThat(pathResponse.getFare().intValue()).isEqualTo(2250);
    }

    @DisplayName("교대역 -> 강남역 -> 양재역 요금을 조회한다. (어린이 할인 추가)")
    @Test
    void 요금_조회_어린이_할인() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse pathResponse = pathService.findShortestPath(source, target, 10);

        assertThat(pathResponse.getFare().intValue()).isEqualTo(1550);
    }

    @DisplayName("교대역 -> 강남역 -> 양재역 요금을 조회한다. (청소년 할인 추가)")
    @Test
    void 요금_조회_청소년_할인() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse pathResponse = pathService.findShortestPath(source, target, 18);

        assertThat(pathResponse.getFare().intValue()).isEqualTo(1970);
    }

    @DisplayName("교대역 -> 강남역 -> 양재역 요금을 조회한다. (성인 할인 없음)")
    @Test
    void 요금_조회_성인_할인() {
        Long source = 1L;
        Long target = 2L;
        when(stationService.findStationById(source)).thenReturn(교대역);
        when(stationService.findStationById(target)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse pathResponse = pathService.findShortestPath(source, target, 30);

        assertThat(pathResponse.getFare().intValue()).isEqualTo(2250);
    }
}
