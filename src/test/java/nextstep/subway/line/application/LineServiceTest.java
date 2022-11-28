package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @DisplayName("노선을 저장 할 수 있다.")
    @Test
    void line_save() {
        //given
        LineService lineService = new LineService(lineRepository, stationService);
        String lineName = "신분당선";
        String lineColor = "bg-red-600";
        LineRequest lineRequest = new LineRequest(lineName, lineColor, 1L, 2L, 10);
        when(lineRepository.save(any())).thenReturn(new Line(lineName, lineColor));

        //when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        //then
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getName()).isEqualTo(lineName);
        assertThat(lineResponse.getColor()).isEqualTo(lineColor);
    }

    @DisplayName("노선 목록을 조회할 수 있다.")
    @Test
    void find_lines() {
        LineService lineService = new LineService(lineRepository, stationService);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(new Line("신분당선", "red"), new Line("구분당선", "blue")));

        List<LineResponse> lines = lineService.findLines();

        assertThat(lines).isNotEmpty();
        assertThat(lines.size()).isEqualTo(2);
        assertThat(lines.get(0).getName()).isEqualTo("신분당선");
        assertThat(lines.get(1).getName()).isEqualTo("구분당선");
    }

    @DisplayName("노선을 조회할 수 있다.")
    @Test
    void find_line() {
        LineService lineService = new LineService(lineRepository, stationService);
        when(lineRepository.findById(any())).thenReturn(Optional.of(new Line("신분당선", "red")));

        LineResponse lines = lineService.findLineResponseById(1L);

        assertThat(lines).isNotNull();
        assertThat(lines.getName()).isEqualTo("신분당선");
        assertThat(lines.getColor()).isEqualTo("red");
    }

}
