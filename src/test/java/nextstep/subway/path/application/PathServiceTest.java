package nextstep.subway.path.application;

import static java.util.stream.Collectors.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.SectionTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("경로 찾기 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private PathService pathService;

    @Mock
    LineService lineService;
    @Mock
    StationService stationService;

    private Line 신분당선;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);

        신분당선 = mock(Line.class);
        이호선 = mock(Line.class);
        // 양재-강남-교대
    }

    @Test
    @DisplayName("최단 경로를 찾고 결과 확인")
    void getShortestPath() {
        // given
        when(lineService.findLinesEntities()).thenReturn(Stream.of(신분당선, 이호선).collect(toList()));
        when(stationService.findStationEntities()).thenReturn(Stream.of(양재역, 강남역, 교대역).collect(toList()));
        when(stationService.findStationById(양재역.getId())).thenReturn(양재역);
        when(stationService.findStationById(교대역.getId())).thenReturn(교대역);
        when(신분당선.getSections()).thenReturn(Stream.of(SectionTest.강남_양재_100).collect(toList()));
        when(이호선.getSections()).thenReturn(Stream.of(SectionTest.강남_교대_30).collect(toList()));

        // when
        PathResponse pathResponse = pathService.getShortestPath(양재역.getId(), 교대역.getId());

        // then
        StationResponse[] expectedStations = Stream.of(양재역, 강남역, 교대역)
            .map(StationResponse::of)
            .toArray(StationResponse[]::new);
        assertThat(pathResponse.getDistance()).isEqualTo(130);
        assertThat(pathResponse.getStations().size()).isEqualTo(3);
        assertThat(pathResponse.getStations()).containsExactly(expectedStations);
    }
}
