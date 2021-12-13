package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.DefaultOverFare;
import nextstep.subway.path.domain.OverFare;
import nextstep.subway.path.infra.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final OverFare overFare;

    public PathService(LineRepository lineRepository, StationService stationService, PathFinder pathFinder, DefaultOverFare overFare) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.overFare = overFare;
    }

    public PathResponse findShortestPath(long source, long target) {

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        List<Line> lines = lineRepository.findAll();

        ShortestPath shortestPath = pathFinder.findShortestPath(lines, sourceStation, targetStation);
        return new PathResponse(shortestPath.findPaths(), shortestPath.findWeight(), overFare.calculate(shortestPath.findWeight()));
    }
}
