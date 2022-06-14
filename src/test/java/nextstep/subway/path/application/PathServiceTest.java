package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

@DisplayName("경로 조회 서비스")
@SpringBootTest
public class PathServiceTest {
    @Autowired
    PathService pathService;
    @MockBean
    StationRepository stationRepository;
    @MockBean
    LineRepository lineRepository;
    @MockBean
    PathFinder pathFinder;

    Long sourceId;
    Long targetId;
    Station source;
    Station target;
    Station mid;
    List<Station> stations;
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
        mid = new Station("남부터미널역");
        stations = Arrays.asList(source, target, mid);
        when(stationRepository.findAll()).thenReturn(stations);
        when(stationRepository.findById(sourceId)).thenReturn(Optional.of(source));
        when(stationRepository.findById(targetId)).thenReturn(Optional.of(target));
    }

    void stubLineService() {
        lines = Collections.emptyList();
        when(lineRepository.findAllWithSections()).thenReturn(lines);
    }

    void stubPathFinder() {
        pathResponse = new PathResponse(Arrays.asList(StationResponse.of(source), StationResponse.of(mid), StationResponse.of(target)), 19);
        when(pathFinder.findPath(stations, lines, source, target)).thenReturn(pathResponse);
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
