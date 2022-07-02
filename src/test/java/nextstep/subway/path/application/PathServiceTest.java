package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class PathServiceTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    @MockBean
    private StationService stationService;

    @MockBean
    private LineService lineService;

    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                  |
     * *3호선(5)*                     *신분당선* (6)
     * |                                |
     * 남부터미널역 --- *3호선 (3)* ---   양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");

        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10, 0);
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 6, 0);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 남부터미널역, 5, 0);

        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 3));
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void getDijkstraShortestPath() {
        // given
        List<Line> lines = new ArrayList<>();
        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);

        PathService pathService = new PathService(lineService, stationService);

        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(남부터미널역.getId())).willReturn(남부터미널역);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(남부터미널역.getId(), 강남역.getId());

        // then
        assertEquals(9, pathResponse.getDistance());
    }
}
