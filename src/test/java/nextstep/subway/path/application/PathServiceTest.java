package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PathServiceTest {

    private static final Line 이호선 = new Line("이호선", "red");
    private static final Station 교대역 = new Station("교대역");
    private static final Station 양재역 = new Station("양재역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Sections 구간 = new Sections(Arrays.asList(
            new Section(이호선, 교대역, 선릉역, 10),
            new Section(이호선, 선릉역, 양재역, 10)));

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
        given(pathFinder.findShortestPath(구간, 교대역, 양재역)).willReturn(new PathResponse(Arrays.asList(교대역, 선릉역, 양재역), 20));

        PathResponse response = pathService.findShortestPath(source, target);

        assertAll(
                () -> assertThat(response.getStations()).containsExactly(StationResponse.of(교대역), StationResponse.of(선릉역), StationResponse.of(양재역)),
                () -> assertThat(response.getDistance()).isEqualTo(20)
        );
    }
}