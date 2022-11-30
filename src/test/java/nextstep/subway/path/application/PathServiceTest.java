package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    Station 왕십리;
    Station 신당;
    Station 행당;
    Station 청구;
    Station DDP;

    Lines lines;

    @BeforeEach
    void setup() {
        왕십리 = Station.of(1L, "왕십리");
        신당 = Station.of(2L, "신당");
        행당 = Station.of(3L, "행당");
        청구 = Station.of(4L, "청구");
        DDP = Station.of(5L, "DDP");

        Line LINE_2 = givenLine(Arrays.asList(
            Section.of(1L, 왕십리, 신당, 10),
            Section.of(2L, 신당, DDP, 10)
        ));
        Line LINE_5 = givenLine(Arrays.asList(
            Section.of(3L, 왕십리, 행당, 10),
            Section.of(4L, 행당, 청구, 10),
            Section.of(5L, 청구, DDP, 10)
        ));

        lines = Lines.from(Arrays.asList(LINE_2, LINE_5));
    }

    @Test
    void findPath() {
        // given
        Long source = 1L;
        Long target = 5L;

        given(lineService.findAllLines()).willReturn(lines);
        given(stationService.findStationById(source)).willReturn(왕십리);
        given(stationService.findStationById(target)).willReturn(DDP);

        // when
        PathResponse response = pathService.findPath(source, target);

        // then
        assertThat(response.getStations()).hasSize(3);
        assertThat(response.getDistance()).isEqualTo(20);
    }

    private Line givenLine(List<Section> sections) {
        Line line = Mockito.mock(Line.class);
        given(line.getSections()).willReturn(
            Sections.from(sections)
        );
        return line;
    }
}
