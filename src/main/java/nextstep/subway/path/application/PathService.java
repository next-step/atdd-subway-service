package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public Path getShortestPath(int source, int target, int age){
        Station sourceStation = stationService.findStationById((long) source);
        Station targetStation = stationService.findStationById((long) target);

        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.from(lines);
        System.out.println("pathFinder = " + pathFinder);
        return pathFinder.getShortestPath(sourceStation, targetStation, age);
    }



}
