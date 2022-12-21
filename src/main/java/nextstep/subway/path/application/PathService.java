package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathFinderRequest;
import nextstep.subway.path.domain.PathFinderResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(
        LineService lineService,
        StationService stationService,
        PathFinder pathFinder
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        final PathFinderRequest request = PathFinderRequest.from(
            lineService.findLinesAsDomainEntity(),
            stationService.findStationByIdAsDomainEntity(source),
            stationService.findStationByIdAsDomainEntity(target)
        );
        final PathFinderResponse response = pathFinder.findShortestPath(request);
        return PathResponse.from(response);
    }
}
