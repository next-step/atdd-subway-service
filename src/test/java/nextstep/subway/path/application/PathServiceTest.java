package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.PathTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


class PathServiceTest extends PathTestUtils {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    private PathService pathService;
    private PathFinder pathFinder;


    /**
     *              (10)
     * 교대역    --- *2호선* ---       강남역
     * |                                |
     * *3호선*(3)                    *신분당선*(10)
     * |                               |
     * 남부터미널역  --- *3호선*(2) ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();
        pathFinder = new PathFinder(stationService, lineService);
        pathService = new PathService(stationService, pathFinder);
    }

    @Test
    @DisplayName("시작역과 도착역의 최단거리를 구하는 서비스 기능 테스트 : 교대역에서 양재역까지 최단거리")
    void getDijkstraSortestPath() {
        // given
        Station 시작점 = 교대역;
        Station 도착점 = 양재역;
        given(stationService.findById(시작점.getId())).willReturn(stationRepository.findById(시작점.getId()).get());
        given(stationService.findById(도착점.getId())).willReturn(stationRepository.findById(도착점.getId()).get());
        given(stationService.findAll()).willReturn(stationRepository.findAll());
        given(lineService.findAllSection()).willReturn(sectionRepository.findAll());

        // when
        PathResponse response = pathService.findDijkstraPath(시작점.getId(), 도착점.getId());

        // then
        assertThat(response.getStations().size()).isEqualTo(3);
        assertThat(response.getDistance()).isEqualTo(5);
    }


}