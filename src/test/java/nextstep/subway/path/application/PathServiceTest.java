package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("지하철 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 석촌역;
    private int age = 19;

    /**
     * 교대역       --- *2호선(10)* ---   강남역
     * |                                    |
     * *3호선(3)*                       *신분당선(10)*
     * |                                    |
     * 남부터미널역  --- *3호선(2)* ---     양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        석촌역 = new Station("석촌역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
    }

    @Test
    void 출발역과_도착역이_같은_경우_최단_경로_조회() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(삼호선, 이호선));

        PathResponse response = pathService.findShortestPath(1L, 2L, age);

        assertAll(
                () -> assertThat(response.getStations()).hasSize(2),
                () -> assertThat(response.getDistance()).isEqualTo(5)
        );
    }

    @Test
    void 출발역과_도착역이_같은_경우_예외_발생() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(삼호선, 이호선));

        assertThatThrownBy(() -> pathService.findShortestPath(1L, 1L, age))
                        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우_예외_발생() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(석촌역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(삼호선, 이호선));

        assertThatThrownBy(() -> pathService.findShortestPath(1L, 2L, age))
                        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 존재하지_않는_출발역이나_도착역을_조회_할_경우_예외_발생() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pathService.findShortestPath( 1L, 0L, age))
                .isInstanceOf(RuntimeException.class);
    }
}
