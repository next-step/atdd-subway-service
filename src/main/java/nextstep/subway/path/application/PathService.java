package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResult;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

public class PathService {

    private final PathFinder pathFinder;
    private final LineService lineService;
    private final StationService stationService;

    public PathService(PathFinder pathFinder, LineService lineService, StationService stationService) {
        this.pathFinder = pathFinder;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResult findPath(long sourceId, long targetId) {
        PathFinderResult result = pathFinder.find(lineService.findAll(), sourceId, targetId);
        return new PathResult(toStations(result), result.getDistance());
    }

    private List<Station> toStations(PathFinderResult result) {
        return result.getStationsIds().stream()
            .map(stationService::findStationById)
            .collect(Collectors.toList());
    }
}
