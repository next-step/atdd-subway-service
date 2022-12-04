package nextstep.subway.path.application;

import nextstep.subway.fare.policy.DistanceFare;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station departureStation = stationService.findStationById(pathRequest.getDepartureId());
        Station arrivalStation = stationService.findStationById(pathRequest.getArrivalId());
        PathFinder pathFinder = PathFinder.from(Lines.from(lineService.findAll()));
        Path shortestPath = pathFinder.findShortestPath(departureStation, arrivalStation);
        return PathResponse.from(shortestPath, DistanceFare.calculate(shortestPath.distance()));
    }
}
