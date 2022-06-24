package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
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

@SpringBootTest
class PathServiceTest {

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
    Station newStation;
    List<Station> stations;
    List<Line> lines;
    PathResponse pathResponse;

    @BeforeEach
    public void setUp() {
        stubStationService();
        stubLineService();
        stubPathFinder();
    }

    void stubStationService() {
        sourceId = 1L;
        targetId = 2L;
        source = new Station("교대역");
        target = new Station("강남역");
        newStation = new Station("남부터미널역");
        stations = Arrays.asList(source, target, newStation);
        given(stationRepository.findAll()).willReturn(stations);
        given(stationRepository.findById(sourceId)).willReturn(Optional.of(source));
        given(stationRepository.findById(targetId)).willReturn(Optional.of(target));
    }

    void stubLineService() {
        lines = Collections.emptyList();
        given(lineRepository.findAll()).willReturn(lines);
    }

    void stubPathFinder() {
        pathResponse = new PathResponse(Arrays.asList(StationResponse.of(source),
            StationResponse.of(newStation),
            StationResponse.of(target)),
            12);
        List<Section> sections = lineRepository.findAll().stream()
            .map(Line::getSections)
            .map(Sections::getSections)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());

        given(pathFinder.searchShortestPath(sections, source, target)).willReturn(pathResponse);
    }

    @Test
    @DisplayName("경로 조회시 정상 값 반환")
    void searchShortestPath() {
        PathResponse actual = pathService.searchShortestPath(sourceId, targetId);
        assertAll(
            () -> assertThat(actual.getStations()).containsExactlyElementsOf(pathResponse.getStations()),
            () -> assertThat(actual.getDistance()).isEqualTo(pathResponse.getDistance())
        );
    }
}
