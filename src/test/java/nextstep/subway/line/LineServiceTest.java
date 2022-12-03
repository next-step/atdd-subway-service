package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    private Station 강남역;
    private Station 광교역;
    private Line 신분당선;
    private List<Line> 노선_목록;
    @Mock
    private StationService stationService;
    @Mock
    private LineRepository lineRepository;
    @InjectMocks
    private LineService lineService;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        신분당선 = Line.of("신분당선", "bg-red-300", 강남역, 광교역, Distance.from(10));
        노선_목록 = Arrays.asList(신분당선);
    }

    @Test
    void findLines() {
        when(lineRepository.findAll()).thenReturn(노선_목록);

        List<LineResponse> responses = lineService.findLines();

        assertAll(
                () -> assertThat(responses.get(0).getName()).isEqualTo("신분당선"),
                () -> assertThat(responses.get(0).getColor()).isEqualTo("bg-red-300"),
                () -> assertThat(responses.get(0).getStations().get(0).getName()).isEqualTo(강남역.getName()),
                () -> assertThat(responses.get(0).getStations().get(1).getName()).isEqualTo(광교역.getName())
        );
    }

    @Test
    void findLineById() {
        when(lineRepository.findById(신분당선.getId())).thenReturn(Optional.of(신분당선));

        Line line = lineService.findLineById(신분당선.getId());

        assertAll(
                () -> assertThat(line.getName()).isEqualTo("신분당선"),
                () -> assertThat(line.getColor()).isEqualTo("bg-red-300")
        );
    }

    @Test
    void updateLine() {
        when(lineRepository.findById(신분당선.getId())).thenReturn(Optional.of(신분당선));
        LineRequest request = new LineRequest("구분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        lineService.updateLine(신분당선.getId(), request);

        Line line = lineService.findLineById(신분당선.getId());
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("구분당선"),
                () -> assertThat(line.getColor()).isEqualTo("bg-red-600")
        );
    }

    @Test
    void deleteLineById() {
        lineService.deleteLineById(신분당선.getId());

        verify(lineRepository, times(1)).deleteById(any());
    }
}
