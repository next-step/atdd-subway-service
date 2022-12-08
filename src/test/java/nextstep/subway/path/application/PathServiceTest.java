package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static nextstep.subway.Fixture.createLine;
import static nextstep.subway.Fixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 칠호선;
    private Station 강남역;
    private Station 광산역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 학동역;
    private Station 강남구청역;
    private int age = 20;

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1l);
        양재역 = createStation("양재역", 2l);
        교대역 = createStation("교대역", 3l);
        남부터미널역 = createStation("남부터미널역", 4l);
        광산역 = createStation("광산역", 7l);
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
        삼호선.addSection(new Section(삼호선, 광산역, 교대역, 5));
        학동역 = createStation("강남역", 10l);
        강남구청역 = createStation("강남역", 11l);
        칠호선 = createLine("칠호선", "bg-green-600", 학동역, 강남구청역, 10);
    }

    @DisplayName("출발역과 도착역이 같으면 예외발생")
    @Test
    void returnsExceptionWithSameStations() {
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선));

        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 강남역.getId(), age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역 다른 경우만 조회할 수 있습니다");
    }

    @DisplayName("존재하지 않는 역을 조회하면 예외발생")
    @Test
    void returnsExceptionWithNoneExistsStartStation() {
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 양재역.getId(), age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역이 존재하지 않습니다");
    }

    @DisplayName("출발역과 도착역이 연결되지않으면 예외발생")
    @Test
    void returnsExceptionWithNoneLinkStation() {
        when(stationRepository.findById(학동역.getId())).thenReturn(Optional.of(학동역));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선));

        assertThatThrownBy(() -> pathService.findShortestPath(학동역.getId(), 강남역.getId(), age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결되있어야 합니다");
    }

    @DisplayName("출발역과 도착역이 연결된상태로 존재하면 최단거리 반환")
    @Test
    void returnsShortestPath() {
        when(stationRepository.findById(광산역.getId())).thenReturn(Optional.of(광산역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse pathResponse = pathService.findShortestPath(광산역.getId(), 양재역.getId(), age);
        assertAll(
                () -> assertThat(pathResponse.getStations().stream().map(StationResponse::getId)).containsExactly(광산역.getId(), 교대역.getId(), 남부터미널역.getId(), 양재역.getId()),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10));
    }
}
