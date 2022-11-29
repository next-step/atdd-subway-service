package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    LineService lineService;

    @Mock
    StationService stationService;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    Line 이호선;
    Line 삼호선;
    Line 신분당선;
    List<Line> lines;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange", 교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);
        신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 10);
        lines = new ArrayList<>();
        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);
    }

    @Test
    void findShortestPath() {
        PathRequest request = new PathRequest(4L, 1L);
        when(lineService.findAll()).thenReturn(lines);
        when(stationService.findStationById(request.getSource())).thenReturn(남부터미널역);
        when(stationService.findStationById(request.getTarget())).thenReturn(강남역);

        PathService pathService = new PathService(lineService, stationService);
        PathResponse shortestPath = pathService.findShortestPath(request);

        List<String> stationNames = shortestPath.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly(남부터미널역.getName(), 양재역.getName(), 강남역.getName());
        assertThat(shortestPath.getDistance()).isEqualTo(13);
    }

}
