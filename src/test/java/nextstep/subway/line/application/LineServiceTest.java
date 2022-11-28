package nextstep.subway.line.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(lineResponse).isNotNull();
        Assertions.assertThat(lineResponse.getName()).isEqualTo(lineName);
        Assertions.assertThat(lineResponse.getColor()).isEqualTo(lineColor);
    }
}
