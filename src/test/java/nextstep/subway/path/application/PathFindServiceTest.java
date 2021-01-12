package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PathFindServiceTest {
    private LineService mockLineService = mock(LineService.class);
    private PathFindService pathFindService;

    @BeforeEach
    void setUp() {
        pathFindService = new PathFindService(mockLineService);
    }

    @DisplayName("최단거리 찾기")
    @Test
    void fineShortestPath() {
        Station 강남 = new Station("강남");
        Station 광교 = new Station("광교");
        Station 교대 = new Station("교대");
        Station 양재 = new Station("양재");
        Set<Station> stations = new HashSet<>(Arrays.asList(강남, 광교, 교대, 양재));
        Line orange = new Line("3호선", "orange");
        Line red = new Line("분당선", "red");
        Section 강남교대구간 = new Section(orange, 강남, 교대, 5);
        Section 강남양재구간 = new Section(red, 강남, 양재, 5);
        Section 교대양재구간 = new Section(red, 교대, 양재, 1);
        Section 양재광교구간 = new Section(red, 양재, 광교, 10);
        List<Section> sections = Arrays.asList(강남교대구간, 강남양재구간, 교대양재구간, 양재광교구간);
        when(mockLineService.getAllStations()).thenReturn(stations);
        when(mockLineService.getAllSections()).thenReturn(sections);

        PathResponse shortestPath = pathFindService.findShortestPath(강남, 광교);

        assertThat(shortestPath.getDistance()).isEqualTo(15);
        assertThat(shortestPath.getStations())
                .containsExactly(StationResponse.of(강남),StationResponse.of(양재),StationResponse.of(광교));
    }
}