package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.path.PathFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PathServiceTest {

    private StationService stationService;
    private LineService lineService;
    private PathService pathService;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        lineService = mock(LineService.class);
        stationService = mock(StationService.class);
        pathFinder = mock(PathFinder.class);
        pathService = new PathService(lineService, stationService, pathFinder);
    }

    @Test
    void findShortestPath() {
        Long source = 1L;
        Long target = 3L;
        given(stationService.findById(source)).willReturn(교대역);
        given(stationService.findById(target)).willReturn(양재역);
        given(lineService.getSections()).willReturn(구간);
        given(pathFinder.findShortestPath(구간, 교대역, 양재역)).willReturn(new PathResult(Arrays.asList(교대역, 선릉역, 양재역), 20));

        PathResponse response = pathService.findShortestPath(source, target);

        assertAll(
                () -> assertThat(response.getStations()).containsExactly(StationResponse.of(교대역), StationResponse.of(선릉역), StationResponse.of(양재역)),
                () -> assertThat(response.getDistance()).isEqualTo(20)
        );
    }
}