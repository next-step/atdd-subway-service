package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station upStation = stationService.findStationById(source);
        Station downStation = stationService.findStationById(target);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder();
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(lines, upStation, downStation);
        return new PathResponse(shortestPath);
    }
}
