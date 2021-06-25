package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.VertexStationResponse;
import nextstep.subway.station.domain.StationRepository;

/**
 * PathService 기능 테스트 코드 작성
 */
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    private Station gangnam;
    private Station gyodae;
    private Station seocho;
    private Line line2;
    private Line line3;
    private Line line4;
    private Line line7;
    private Station bangbae;
    private Station sadang;
    private Station nakseongdae;
    private Station goter;
    private Station chongshin;
    private Station naebang;

    @BeforeEach
    void setUp() {
        gangnam = new Station("강남역");
        gyodae = new Station("교대역");
        seocho = new Station("서초역");
        bangbae = new Station("방배역");
        sadang = new Station("사당역");
        nakseongdae = new Station("낙성대역");
        goter = new Station("고속터미널역");
        chongshin = new Station("총신대입구역");
        naebang = new Station("내방역");

        line2 = new Line("2호선", "green", gangnam, gyodae, 3);
        line2.addSection(new Section(line2, gyodae, seocho, 2));
        line2.addSection(new Section(line2, seocho, bangbae, 2));
        line2.addSection(new Section(line2, bangbae, sadang, 2));
        line2.addSection(new Section(line2, sadang, nakseongdae, 2));

        line3 = new Line("3호선", "orange", gyodae, goter, 2);
        line4 = new Line("4호선", "blue", sadang, chongshin, 3);
        line7 = new Line("7호선", "dark_green", chongshin, naebang, 5);
        line7.addSection(new Section(line7, naebang, goter, 3));
    }

    @Test
    @DisplayName("최단경로 찾기 서비스")
    void findShortestPath() {
        // given
        List<Line> lines = Arrays.asList(line2, line3, line4, line7);

        // mocking
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationService.findById(1L)).thenReturn(gangnam);
        when(stationService.findById(3L)).thenReturn(chongshin);

        // when
        PathResponse shortestPath = pathService.findShortestPath(1L, 3L);

        // then
        assertAll(
                () -> {
                    List<String> stationNames = shortestPath.getStations()
                            .stream()
                            .map(VertexStationResponse::getName)
                            .collect(Collectors.toList());
                    List<String> targetStationNames = Stream.of(gangnam, gyodae, seocho, bangbae, sadang, chongshin)
                            .map(Station::getName)
                            .collect(Collectors.toList());
                    assertThat(Arrays.equals(stationNames.toArray(), targetStationNames.toArray())).isTrue();
                },
                () -> {
                    int totalDistance = shortestPath.getDistance();
                    assertThat(totalDistance).isEqualTo(12);
                }
        );
    }
}
