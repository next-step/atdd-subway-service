package nextstep.subway.path.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    /**
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
        이호선 = createLine("이호선", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", 교대역, 양재역, 20);
        신분당선 = createLine("신분당선", 강남역, 양재역, 5);
        삼호선.addSection(new Section(교대역, 남부터미널역, 15));
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로를 조회할 수 있다.")
    @Test
    void findShortestPath() {
        Long source = 1L;
        Long target = 2L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(남부터미널역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(강남역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 삼호선, 신분당선));

        PathResponse shortestPath = pathService.findShortestPath(source, target);

        assertThat(shortestPath.getDistance()).isEqualTo(10);
        List<String> actualStationNames = shortestPath.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(actualStationNames).containsExactly("남부터미널역", "양재역", "강남역");
    }
}
