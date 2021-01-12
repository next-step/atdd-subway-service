package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    LineRepository lineRepository;

    @Mock
    StationService stationService;

    private Station 강남역;
    private Station 역삼역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 역삼역, 10, 0);
    }

    @Test
    void saveLine() {
        //given
        when(lineRepository.save(any()))
                .thenReturn(신분당선);
        LineService lineService = new LineService(lineRepository, stationService);

        //when
        LineRequest lineRequest = new LineRequest(신분당선.getName(), 신분당선.getColor(), 강남역.getId(), 역삼역.getId(), 10);
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        //then
        지하철_노선조회_검증(lineResponse, "신분당선", "bg-red-600", Arrays.asList(강남역.getId(), 역삼역.getId()));
    }

    @Test
    void findLines() {
        //given
        when(lineRepository.findAll())
                .thenReturn(Arrays.asList(신분당선, new Line("2호선", "bg-green-300")));
        LineService lineService = new LineService(lineRepository, stationService);

        //when
        List<LineResponse> lineResponses = lineService.findLines();

        //then
        assertThat(lineResponses.size()).isEqualTo(2);
        지하철_노선조회_검증(lineResponses.get(0), "신분당선", "bg-red-600", Arrays.asList(강남역.getId(), 역삼역.getId()));
        지하철_노선조회_검증(lineResponses.get(1), "2호선", "bg-green-300", Collections.emptyList());
    }

    @Test
    void findLineResponseById() {
        //given
        when(lineRepository.findById(any()))
                .thenReturn(Optional.of(신분당선));
        LineService lineService = new LineService(lineRepository, stationService);

        //when
        LineResponse lineResponse = lineService.findLineResponseById(신분당선.getId());

        //then
        지하철_노선조회_검증(lineResponse, "신분당선", "bg-red-600", Arrays.asList(강남역.getId(), 역삼역.getId()));
    }

    @Test
    void updateLine() {
        //given
        Line findMockLine = mock(Line.class);
        when(lineRepository.findById(any()))
                .thenReturn(Optional.of(findMockLine));
        LineService lineService = new LineService(lineRepository, stationService);

        //when
        LineRequest lineRequest = new LineRequest("2호선", "남색", 1L, 2L, 10);
        lineService.updateLine(findMockLine.getId(), lineRequest);

        //then
        ArgumentCaptor<Line> argumentCaptor = ArgumentCaptor.forClass(Line.class);
        verify(findMockLine).update(argumentCaptor.capture());

        Line captorLine = argumentCaptor.getValue();
        assertThat(captorLine.getName()).isEqualTo("2호선");
        assertThat(captorLine.getColor()).isEqualTo("남색");
    }

    @Test
    void deleteLineById() {
        //given
        LineService lineService = new LineService(lineRepository, stationService);
        //when

        lineService.deleteLineById(1L);

        //then
        verify(lineRepository).deleteById(1L);
    }

    private void 지하철_노선조회_검증(LineResponse lineResponse, String name, String color, List<Long> stationIds) {
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getName()).isEqualTo(name);
        assertThat(lineResponse.getColor()).isEqualTo(color);
        assertThat(
                lineResponse.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList())
        ).containsExactlyInAnyOrderElementsOf(stationIds);
    }
}