package nextstep.subway.path;


import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private PathService pathService;

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;


    /**
     * 교대역 -- *2호선* 10 --- 강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 3                        10
     * |                        |
     * 남부터미널역 -- *3호선* 2 -양재
     */

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
        pathService = new PathService(stationService,lineService,new PathFinder());
    }

    @DisplayName("가짜 객체를 사용하여 findPaths 검증")
    @Test
    void findPaths() {
        // given
        when(stationService.findStationById(any())).thenReturn(교대역, 양재역);
        when(lineService.findLines()).thenReturn(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // when
        PathResponse paths = pathService.findPaths(1L, 2L);

        // then
        assertThat(paths.getDistance()).isEqualTo(5);
        assertThat(paths.getStations()).extracting(StationResponse::getName)
                .containsExactly("교대역", "남부터미널역", "양재역");
    }
}
