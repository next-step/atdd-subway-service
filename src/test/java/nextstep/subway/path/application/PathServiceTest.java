package nextstep.subway.path.application;

import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;

    @Mock
    private StationService stationService;

    @Mock
    private LineQueryService lineQueryService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private List<Line> lines;

    /**
     * 교대역      --- *2호선* ---   강남역
     * |                              |
     * *3호선*                   *신분당선*
     * |                              |
     * 남부터미널역  --- *3호선* ---    양재
     * 교대-(10)-강남
     * (3)     (10)
     * 남부-(2)-양재
     */
    @BeforeEach
    public void setUp() {
        pathService = new PathService(stationService, lineQueryService);

        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        양재역 = new Station("양재역");
        ReflectionTestUtils.setField(양재역, "id", 2L);
        교대역 = new Station("교대역");
        ReflectionTestUtils.setField(교대역, "id", 3L);
        남부터미널역 = new Station("남부터미널역");
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "bg-red-500", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "bg-red-400", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);

        lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    }

    @Test
    void findPath() {
        //given
        when(lineQueryService.findLines()).thenReturn(lines);
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(남부터미널역.getId())).thenReturn(남부터미널역);
        PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());

        //when
        PathResponse actual = pathService.findPath(pathRequest);

        //then
        assertThat(actual.getDistance()).isEqualTo(12);
        assertThat(actual.getStations()).containsExactly(
                StationResponse.of(강남역),
                StationResponse.of(양재역),
                StationResponse.of(남부터미널역)
        );
    }
}
