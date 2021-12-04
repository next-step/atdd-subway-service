package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * packageName : nextstep.subway.path
 * fileName : MockitoTest
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
public class PathMockitoTest {
    private Station 강남역 = mock(Station.class);
    private Station 역삼역 = mock(Station.class);

    @BeforeEach
    void setUp() {
        when(강남역.getName()).thenReturn("강남역");
        when(강남역.getId()).thenReturn(1L);
        when(역삼역.getName()).thenReturn("역삼역");
        when(역삼역.getId()).thenReturn(2L);
        when(강남역.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(강남역.getModifiedDate()).thenReturn(LocalDateTime.now());
        when(역삼역.getCreatedDate()).thenReturn(LocalDateTime.now());
        when(역삼역.getModifiedDate()).thenReturn(LocalDateTime.now());
    }

    @Test
    @DisplayName("경로 조회")
    public void findPaths() throws Exception {
        //given
        StationRepository stationRepository = mock(StationRepository.class);
        LineRepository lineRepository = mock(LineRepository.class);
        PathFinder pathFinder = mock(PathFinder.class);

        List<Line> lines = Lists.newArrayList(
                Line.of("1호선", "남색", 강남역, 역삼역, 5));

        List<Station> stations = Lists.newArrayList(강남역, 역삼역);


        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAll()).thenReturn(stations);

        when(pathFinder.getShortestPath(lines, stations, 1L, 2L))
                .thenReturn(PathResponse.of(
                        Arrays.asList(new Station("강남역"), new Station("역삼역"))));

        PathService pathService = new PathService(pathFinder, stationRepository, lineRepository);

        // when
        PathResponse response = pathService.getShortestPath(1L, 2L);

        // then
        assertThat(response.getStations()).hasSize(2);
    }
}
