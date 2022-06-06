package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.collections.Lines;
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

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long stationId, Long targetId) {
        Station source = stationService.findStationById(stationId);
        Station target = stationService.findStationById(targetId);
        Lines lines = new Lines(lineRepository.findAll());

        GraphPath<Station, DefaultWeightedEdge> shortestPath = lines.findShortestPath(source, target);
        List<Station> routes = shortestPath.getVertexList();
        int distance = (int) shortestPath.getWeight();
        return PathResponse.of(routes, distance);
    }
}
