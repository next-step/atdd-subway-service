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

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("경로 찾기 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    LineRepository lines;
    @Mock
    StationRepository stations;

    private Line 신분당선;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        신분당선 = mock(Line.class);
        이호선 = mock(Line.class);
        // 양재-강남-교대
    }

    @Test
    void getShortestPath() {
        // given
        when(lines.findAll()).thenReturn(Stream.of(신분당선, 이호선).collect(toList()));
        when(신분당선.getSections()).thenReturn(Stream.of(SectionTest.강남_양재_100).collect(toList()));
        when(이호선.getSections()).thenReturn(Stream.of(SectionTest.강남_교대_30).collect(toList()));
        PathService pathService = new PathService(lines, stations);

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
