package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private StationService stationService;
    @Mock
    private LineRepository lineRepository;
    @InjectMocks
    private PathService pathService;

    private Station 강남역, 정자역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        정자역 = new Station("정자역");
        신분당선 = new Line("신분당선", "red", 강남역, 정자역, 6);
    }

    @Test
    @DisplayName("Mockito 사용하여 최단 경로 찾기 테스트")
    public void 최단_경로_찾기() {
        //given
        PathRequest 경로검색 = new PathRequest(1L, 2L);

        //when
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(정자역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선));

        //then
        assertThat(pathService.findShortestPath(경로검색).getDistance()).isEqualTo(6);
    }
}
