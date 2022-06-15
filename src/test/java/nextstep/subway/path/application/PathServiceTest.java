package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.PathResponse;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private PathFinder pathFinder;
    @Mock
    private StationService stationService;
    @Mock
    private LineService lineService;
    @InjectMocks
    private PathService pathService;

    @Test
    void 최단_경로를_찾을_수_있다() {
        //given
        given(stationService.findStationById(any())).willReturn(new Station("강남역"));
        given(lineService.findLines()).willReturn(new Lines());
        given(pathFinder.getShortestPath(any(), any(), any())).willReturn(new PathResponse());

        //when
        PathResponse pathResponse = pathService.get(0, 0);

        //then
        assertThat(pathResponse.getDistance()).isZero();
        assertThat(pathResponse.getStations()).isEmpty();
    }
}
