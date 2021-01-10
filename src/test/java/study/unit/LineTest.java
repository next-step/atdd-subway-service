package study.unit;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("노선 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class LineTest {
    @Mock
    private LineService lineService;

    private List<Line> lines;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "GREEN", new Station("강남역"), new Station("선릉역"), 10);
        삼호선 = new Line("3호선", "GREEN", new Station("수서역"), new Station("압구정역"), 10);
        lines = Arrays.asList(이호선, 삼호선);
    }

    @DisplayName("노선 목록 조회")
    @Test
    void findLines() {
        when(lineService.findLines()).thenReturn(LineResponse.ofList(lines));

        List<LineResponse> result = lineService.findLines();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(LineResponse::getName).containsExactly("2호선", "3호선");
    }

    @DisplayName("노선 구간 지하철역 조회")
    @Test
    void findLine() {
        when(lineService.findLineResponseById(1L)).thenReturn(LineResponse.of(이호선, StationResponse.ofList(이호선.getStations())));

        LineResponse result = lineService.findLineResponseById(1L);

        assertThat(result.getStations()).extracting(StationResponse::getName).containsExactly("강남역", "선릉역");
    }
}
