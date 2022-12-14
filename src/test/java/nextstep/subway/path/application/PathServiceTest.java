package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.domain.PathFinder;
import nextstep.subway.path.vo.Path;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.utils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;


@DisplayName("경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 분당선;
    private Line 삼호선;
    private Line 팔호선;
    private Station 정자역;
    private Station 양재역;
    private Station 수서역;
    private Station 서현역;
    private Station 잠실역;
    private Station 복정역;
    private List<Section> sections;
    private PathFinder pathFinder;


    /**
     * 양재역 ------*3호선(5)*------ 수서역
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * *신분당선(10)*             *분당선(5)*
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * 정쟈역 ------*분당선(5)*------ 서현역
     * 잠실역 ------*팔호선(20)*----- 복정역
     */
    @BeforeEach
    void setUp() {
        정자역 = new Station("정자역");
        양재역 = new Station("양재역");
        수서역 = new Station("수서역");
        서현역 = new Station("서현역");
        잠실역 = new Station("잠실역");
        복정역 = new Station("복정역");

        신분당선 = new Line("신분당선", "red", 양재역, 정자역, 10);
        분당선 = new Line("분당선", "yellow", 수서역, 정자역, 10);
        삼호선 = new Line("삼호선", "orange", 양재역, 수서역, 5);
        팔호선 = new Line("팔호선", "pink", 잠실역, 복정역, 20);

        분당선.addSection(new Section(분당선, 서현역, 정자역, 5));

        sections = new ArrayList<>();
        sections.addAll(신분당선.getSections());
        sections.addAll(분당선.getSections());
        sections.addAll(삼호선.getSections());
        sections.addAll(팔호선.getSections());

        pathFinder = PathFinder.from(sections);
    }


    @DisplayName("출발역과 도착역 사이의 최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        PathFinder pathFinder = PathFinder.from(sections);

        Path path = pathFinder.findAllStationsByStations(양재역, 서현역);

        assertAll(
                () -> assertThat(path.getStations()).containsExactly(양재역, 수서역, 서현역),
                () -> assertThat(path.getDistance()).isEqualTo(10)
        );
    }


    @DisplayName("연결되지 않은 역의 최단 거리를 조회할 때 예외가 발생한다.")
    @Test
    void findShortestPathNotConnectedException() {
        when(stationService.findStationById(1L)).thenReturn(수서역);
        when(stationService.findStationById(2L)).thenReturn(잠실역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 분당선, 삼호선, 팔호선));

        Assertions.assertThatThrownBy(() -> pathService.findShortestPath(1L, 2L, 20))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith(INVALID_CONNECTED_STATIONS);
    }


    @DisplayName("출발역과 같은 도착역을 입력하면 예외가 발생한다.")
    @Test
    void findShortestPathInvalidSameStationsException() {
        when(stationService.findStationById(1L)).thenReturn(수서역);
        when(stationService.findStationById(1L)).thenReturn(수서역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 분당선, 삼호선, 팔호선));

        Assertions.assertThatThrownBy(() -> pathService.findShortestPath(1L, 1L, 20))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith(INVALID_SAME_STATIONS);

    }

    @DisplayName("존재하지 않는 역으로 최단 거리를 조회할 때 예외가 발생한다.")
    @Test
    void findShortestPathNotExistsException() {
        when(stationService.findStationById(1L)).thenReturn(수서역);
        when(stationService.findStationById(2L)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> pathService.findShortestPath(1L, 2L, 20))
                .isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith(NOT_EXISTS_STATION);
    }


}
