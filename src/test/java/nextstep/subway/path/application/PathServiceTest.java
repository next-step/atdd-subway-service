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

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private SectionRepository sectionRepository;

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 신도림역 = new Station("신도림역");
    private final Station 양재역 = new Station("양재역");
    private final Station 교대역 = new Station("교대역");
    private List<Station> stations;
    private Line line;

    @BeforeEach
    void setUp() {
        stations = new ArrayList<>();
        stations.add(강남역);
        stations.add(역삼역);
        stations.add(양재역);
        stations.add(교대역);

        line = new Line("2호선", "green", 강남역, 역삼역, 1);
        line.addSection(new Section(line, 역삼역, 양재역, 2));
        line.addSection(new Section(line, 양재역, 교대역, 3));
        line.addSection(new Section(line, 교대역, 신도림역, 5));
    }

    @DisplayName("최단 경로 초회 기능 테스트")
    @Test
    void findShortestPath() {
        // given
        when(sectionRepository.findAll()).thenReturn(line.getSections());
        final long source = 1;
        final long target = 2;
        when(stationRepository.findById(source)).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(target)).thenReturn(Optional.ofNullable(교대역));
        final PathService pathService = new PathService(stationRepository, sectionRepository);

        // when
        final PathResponse pathResponse = pathService.findShortestPath(source, target);

        // then
        assertAll(
            () -> assertThat(pathResponse.getDistance()).isEqualTo(6),
            () -> assertThat(pathResponse.getStations()).isEqualTo(stations)
        );
    }
}
