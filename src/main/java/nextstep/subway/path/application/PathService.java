package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
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

    public PathResponse getShortestPath(Long source, Long target) {
        PathFinder pathFinder = new PathFinder(lineService.findAllLines());
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);
        return PathResponse.of(path);
    }
}
