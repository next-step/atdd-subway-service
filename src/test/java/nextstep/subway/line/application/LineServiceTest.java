package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);
    }

    @Test
    @DisplayName("노선을 추가한다")
    void addLineTest() {
        // given
        LineRequest request = new LineRequest("2호선", "green", 1L, 2L, 10);

        // when
        when(lineRepository.save(any(Line.class))).thenReturn(new Line(request.getName(), request.getColor(), new Station("강남역"), new Station("교대역"), request.getDistance()));
        LineResponse response = lineService.saveLine(request);

        // then
        assertThat(response.getStations()).hasSize(2);
    }

    @Test
    @DisplayName("노선을 조회한다")
    void findLineByIdTest() {
        // when
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(new Line()));

        // then
        assertThat(lineService.findLineResponseById(anyLong())).isNotNull();
    }

    @Test
    @DisplayName("노선 목록을 조회한다")
    void findLinesTest() {
        // when
        when(lineRepository.findAll()).thenReturn(Arrays.asList(new Line(), new Line()));

        // then
        assertThat(lineService.findLines()).hasSize(2);
    }
}