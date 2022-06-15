package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest pathRequest) {
        Station sourceStation = stationService.findStationById(pathRequest.getSourceStationId());
        Station targetStation = stationService.findStationById(pathRequest.getTargetStationId());
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());
        GraphPath<Station, DefaultWeightedEdge> path = pathFinder.getShortestPath(sourceStation, targetStation);
        return PathResponse.of(path.getVertexList(), path.getWeight());
    }
}
