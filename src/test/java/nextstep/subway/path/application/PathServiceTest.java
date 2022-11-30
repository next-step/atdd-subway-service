package nextstep.subway.path.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
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

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 김포공항역;
    private Station 마곡나루역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Line 공항선;

    /**
     * 김포공항    --- *공항선(30)* --- 마곡나루
     * 교대역    --- *2호선(10)* ---   강남역
     * |                                 |
     * *3호선(15)*                   *신분당선(5)*
     * |                                |
     * 남부터미널역  --- *3호선(5)* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        김포공항역 = new Station("김포공항역");
        마곡나루역 = new Station("마곡나루역");

        이호선 = createLine("이호선", 교대역, 강남역, 10, 100);
        삼호선 = createLine("삼호선", 교대역, 양재역, 20, 200);
        신분당선 = createLine("신분당선", 강남역, 양재역, 5, 300);
        공항선 = createLine("공항선", 김포공항역, 마곡나루역, 30, 500);

        삼호선.addSection(new Section(교대역, 남부터미널역, 15));
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로와 요금을 조회할 수 있다.")
    @Test
    void findShortestPath() {
        Long source = 1L;
        Long target = 2L;
        given(stationRepository.findById(source)).willReturn(Optional.of(남부터미널역));
        given(stationRepository.findById(target)).willReturn(Optional.of(강남역));
        given(lineRepository.findAll()).willReturn(Arrays.asList(이호선, 삼호선, 신분당선));

        PathResponse shortestPath = pathService.findShortestPath(20, source, target);

        assertThat(shortestPath.getDistance()).isEqualTo(10);
        List<String> actualStationNames = shortestPath.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(actualStationNames).containsExactly("남부터미널역", "양재역", "강남역");
        assertThat(shortestPath.getFare()).isEqualTo(1550);
    }

    @DisplayName("출발역과 도착역이 동일한 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithSameStation() {
        Long source = 1L;
        Long target = 1L;
        given(stationRepository.findById(source)).willReturn(Optional.of(남부터미널역));
        given(lineRepository.findAll()).willReturn(Arrays.asList(이호선, 삼호선, 신분당선));

        assertThatThrownBy(() -> pathService.findShortestPath(20, source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 동일한 경우 경로를 조회할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithNotConnectStation() {
        Long source = 1L;
        Long target = 2L;
        given(stationRepository.findById(source)).willReturn(Optional.of(김포공항역));
        given(stationRepository.findById(target)).willReturn(Optional.of(강남역));
        given(lineRepository.findAll()).willReturn(Arrays.asList(이호선, 삼호선, 신분당선, 공항선));

        assertThatThrownBy(() -> pathService.findShortestPath(20, source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않은 경우 경로를 조회할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 존재하지 않을 경우 최단 경로 조회 시 예외가 발생한다.")
    @Test
    void findShortestPathWithNotExistStation() {
        Long source = 1L;
        Long target = 2L;
        given(stationRepository.findById(source)).willThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> pathService.findShortestPath(20, source, target))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
