package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResponse.PathStationResponse;
import nextstep.subway.station.application.StationService;

@Service
public class PathService {

    private final PathFinder pathFinder;
    private final LineService lineService;
    private final StationService stationService;

    public PathService(PathFinder pathFinder, LineService lineService, StationService stationService) {
        this.pathFinder = pathFinder;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(long sourceId, long targetId) {
        PathFinderResult result = pathFinder.find(lineService.findAll(), sourceId, targetId);
        return new PathResponse(toPathStationResponse(result.getStationsIds()), result.getDistance());
    }

    private List<PathStationResponse> toPathStationResponse(List<Long> stationsIds) {
        return stationsIds.stream()
            .map(stationService::findStationById)
            .map(station -> new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate()))
            .collect(Collectors.toList());
    }
}
