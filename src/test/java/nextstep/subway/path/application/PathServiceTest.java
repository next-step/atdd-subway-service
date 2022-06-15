package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class PathServiceTest {
    @MockBean
    private StationService stationService;

    @MockBean
    private LineRepository lineRepository;

    @MockBean
    private PathFinder pathFinder;

    private Long sourceId;
    private Long targetId;
    private Station source;
    private Station target;
    private PathResponse pathResponse;

    @Test
    @DisplayName("지하철 출발역과 도착역 사이 최단 경로를 탐색한다.")
    void findShortestPath() {
        //given
        stubStationService();
        stubLineRepository();
        stubPathFinder();

        //when
        PathService pathService = new PathService(stationService, lineRepository, pathFinder);
        PathResponse actual = pathService.findShortestPath(sourceId, targetId);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getStations()).isNotNull();
        assertThat(actual.getStations()).containsExactlyElementsOf(pathResponse.getStations());
    }

    private void stubPathFinder() {
        Station stopover = new Station("당산역");
        pathResponse = new PathResponse(Arrays.asList(
                StationResponse.of(source),
                StationResponse.of(stopover),
                StationResponse.of(target)), 8);
        List<Sections> sections = Collections.emptyList();
        when(pathFinder.findShortestPath(sections, source, target)).thenReturn(pathResponse);
    }

    private void stubLineRepository() {
        List<Line> lines = Collections.emptyList();
        when(lineRepository.findAll()).thenReturn(lines);
    }

    private void stubStationService() {
        sourceId = 1L;
        targetId = 2L;
        source = new Station("합정역");
        target = new Station("영등포구청역");
        when(stationService.findById(sourceId)).thenReturn(source);
        when(stationService.findById(targetId)).thenReturn(target);
    }
}