package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
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

        Line LINE_2 = new Line("2호선", "green", 왕십리, DDP, 20);
        LINE_2.addLineStation(왕십리, 신당, 10);
        Line LINE_5 = new Line("2호선", "purple", 왕십리, DDP, 30);
        LINE_5.addLineStation(왕십리, 행당, 10);
        LINE_5.addLineStation(행당, 청구, 10);

        lines = Lines.from(Arrays.asList(LINE_2, LINE_5));
    }

    @Test
    void findPath() {
        // given
        Long source = 1L;
        Long target = 5L;
        LoginMember loginMember = new LoginMember();

        given(lineService.findAllLines()).willReturn(lines);
        given(stationService.findStationById(source)).willReturn(왕십리);
        given(stationService.findStationById(target)).willReturn(DDP);

        // when
        PathResponse response = pathService.findPath(loginMember, source, target);

        // then
        assertThat(response.getDistance()).isEqualTo(20);
        assertThat(response.getStations()).hasSize(3);
        assertThat(response.getAmount()).isEqualTo(1450L);
    }
}
