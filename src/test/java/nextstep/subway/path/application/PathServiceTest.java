package nextstep.subway.path.application;

import nextstep.exception.SameSourceAndTargetException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.path.fixture.PathFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
    void findShortestPath_최단_경로를_조회한다() {
        // given
        Long source = 1L;
        Long target = 3L;
        given(stationService.findById(source)).willReturn(교대역);
        given(stationService.findById(target)).willReturn(양재역);
        given(lineService.getSections()).willReturn(구간);
        given(pathFinder.findShortestPath(구간, 교대역, 양재역)).willReturn(new PathResult(Arrays.asList(교대역, 선릉역, 양재역), 20));

        // when
        PathResponse response = pathService.findShortestPath(source, target);

        // then
        assertAll(
                () -> assertThat(response.getStations()).containsExactly(StationResponse.of(교대역), StationResponse.of(선릉역), StationResponse.of(양재역)),
                () -> assertThat(response.getDistance()).isEqualTo(20)
        );
    }

    @Test
    void findShortestPath_출발역과_도착역이_같으면_에러를_발생한다() {
        assertThatExceptionOfType(SameSourceAndTargetException.class)
                .isThrownBy(() -> pathService.findShortestPath(1L, 1L));
    }
}