package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.collections.Lines;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Station source, Station target) {
        Lines lines = new Lines(lineRepository.findAll());
        GraphPath<Station, DefaultWeightedEdge> shortestPath = lines.findShortestPath(source, target);
        List<Station> routes = shortestPath.getVertexList();
        int distance = (int) shortestPath.getWeight();
        return PathResponse.of(routes, distance);
    }
}
