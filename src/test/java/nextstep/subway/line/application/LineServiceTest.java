package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);

        when(stationService.findStationById(1L)).thenReturn(new Station("강남역"));
        when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(new Line("2호선", "BLUE", new Station("강남역"), new Station("판교역"), 10)));
    }

    @Test
    void addLineStation() {
        boolean isAddSection = lineService.addLineStation(1L, new SectionRequest(1L, 3L, 5));
        assertThat(isAddSection).isTrue();
    }

    @Test
    void exception_distance() {
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 3L, 15)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception_duplicate_section() {
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 2L, 8)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception_not_registered_station() {
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(4L, 5L, 8)))
                .isInstanceOf(RuntimeException.class);
    }
}
