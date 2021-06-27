package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private SectionRepository sectionRepository;

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final int distance = 100;
    private List<Station> stations;
    private List<Section> sections;

    @BeforeEach
    void setUp() {

        stations = new ArrayList<>();
        stations.add(강남역);
        stations.add(역삼역);

        final Line line = new Line("2호선", "green", 강남역, 역삼역, distance);

        sections = new ArrayList<>();
        sections.add(new Section(line, 강남역, 역삼역, distance));
    }

    @DisplayName("최단 경로 초회 기능 테스트")
    @Test
    void findShortestPath() {
        // given
        when(stationRepository.findAll()).thenReturn(stations);
        when(sectionRepository.findAll()).thenReturn(sections);
        final long source = 1;
        final long target = 2;
        when(stationRepository.findById(source)).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(target)).thenReturn(Optional.ofNullable(역삼역));
        final PathService pathService = new PathService(stationRepository, sectionRepository);

        // when
        final PathResponse pathResponse = pathService.findShortestPath(source, target);

        // then
        assertAll(
            () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
            () -> assertThat(pathResponse.getStations()).isEqualTo(stations)
        );
    }
}
