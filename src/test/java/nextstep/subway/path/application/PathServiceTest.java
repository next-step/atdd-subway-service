package nextstep.subway.path.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        // given
        강남역 = Station.of("강남역");
        양재역 = Station.of("양재역");
        교대역 = Station.of("교대역");
        남부터미널역 = Station.of("남부터미널역");

        신분당선 = Line.from("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = Line.from("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = Line.from("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        삼호선.addSections(Section.of(삼호선, 교대역, 남부터미널역, 3));
    }

    @DisplayName("교대역에서 양재역 가는 최단 경로는 거리가 5인 교대역, 남부터미널역, 양재역이 조회된다")
    @Test
    void 최단경로조회1() {
        // given
        when(stationRepository.findById(any())).thenReturn(Optional.of(교대역), Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when
        PathResponse paths = pathService.findShortestPath(1L, 2L);

        // then
        assertThat(paths.getDistance()).isEqualTo(5);
        assertThat(paths.getStations()).extracting(StationResponse::getName)
            .containsExactly("교대역", "남부터미널역", "양재역");
    }

    @DisplayName("남부터미널역에서 강남역 가는 최단 경로는 거리가 12인 남부터미널역, 양재역, 강남역이 조회된다")
    @Test
    void 최단경로조회2() {
        // given
        when(stationRepository.findById(any())).thenReturn(Optional.of(남부터미널역), Optional.of(강남역));
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when
        PathResponse paths = pathService.findShortestPath(1L, 2L);

        // then
        assertThat(paths.getDistance()).isEqualTo(12);
        assertThat(paths.getStations()).extracting(StationResponse::getName)
            .containsExactly("남부터미널역", "양재역", "강남역");
    }

}