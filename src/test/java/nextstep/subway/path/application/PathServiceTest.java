package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@DisplayName("경로 조회 서비스")
@SpringBootTest
public class PathServiceTest {
    @Autowired
    PathService pathService;
    @MockBean
    StationService stationService;
    @MockBean
    LineService lineService;
    @MockBean
    PathFinder pathFinder;

    Long sourceId;
    Long targetId;
    Station source;
    Station target;
    Station mid;
    List<Line> lines;
    PathResponse pathResponse;

    @BeforeEach
    void setUp() {
        stubStationService();
        stubLineService();
        stubPathFinder();
    }

    void stubStationService() {
        sourceId = 10L;
        targetId = 20L;
        source = new Station("교대역");
        target = new Station("역삼역");
        when(stationService.findById(sourceId)).thenReturn(source);
        when(stationService.findById(targetId)).thenReturn(target);
    }

    void stubLineService() {
        lines = Collections.emptyList();
        when(lineService.findAll()).thenReturn(lines);
    }

    void stubPathFinder() {
        mid = new Station("남부터미널역");
        pathResponse = new PathResponse(Arrays.asList(StationResponse.of(source), StationResponse.of(mid), StationResponse.of(target)), 19);
        when(pathFinder.findPath(lines, source, target)).thenReturn(pathResponse);
    }

    @DisplayName("최단 경로를 조회한다")
    @Test
    void findPath() {
        PathResponse actual = pathService.findPath(sourceId, targetId);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getStations()).containsExactlyElementsOf(pathResponse.getStations());
            softAssertions.assertThat(actual.getDistance()).isEqualTo(pathResponse.getDistance());
        });
    }
}
