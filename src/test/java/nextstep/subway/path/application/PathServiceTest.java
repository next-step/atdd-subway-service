package nextstep.subway.path.application;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, Distance.of(10), 0);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, Distance.of(10), 500);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, Distance.of(5), 900);

        삼호선.addSection(교대역, 남부터미널역, Distance.of(3));
    }

    /**
     * 교대역    --- *2호선(10)* ---   강남역
     * |                           |
     * *3호선(3)*                   *신분당선(10)*
     * |                           |
     * 남부터미널역  --- *3호선(2)* --- 양재역
     */
    @DisplayName("역과 역 사이의 최단 경로를 조회한다.")
    @Test
    void findPath() {
        // given
        Long source = 1L;
        Long target = 2L;
        when(stationRepository.findById(source)).thenReturn(ofNullable(강남역));
        when(stationRepository.findById(target)).thenReturn(ofNullable(남부터미널역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        PathResponse paths = pathService.findPaths(1L, 2L);

        // then
        assertThat(paths.getDistance()).isEqualTo(12);
        assertThat(paths.getStations()).extracting(StationResponse::getName)
                .containsExactly("강남역", "양재역", "남부터미널역");
    }
}
