package nextstep.subway.path.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;
    @Mock
    private PathFinderGenerator pathFinderGenerator;

    @InjectMocks
    private PathService pathService;

    @Test
    void findPath() {
        // given
        Long source = 1L;
        Long target = 5L;

        Lines lines = mock(Lines.class);
        Station sourceStation = mock(Station.class);
        Station targetStation = mock(Station.class);
        PathFinder pathFinder = mock(PathFinder.class);
        given(pathFinder.findPath(sourceStation, targetStation))
            .willReturn(Path.of(Stations.empty(), Distance.zero()));

        given(lineService.findAllLines()).willReturn(lines);
        given(stationService.findStationById(source)).willReturn(sourceStation);
        given(stationService.findStationById(target)).willReturn(targetStation);
        given(pathFinderGenerator.generate(lines)).willReturn(pathFinder);

        // when
        pathService.findPath(source, target);

        // then
        then(pathFinder).should().findPath(sourceStation, targetStation);
    }
}
