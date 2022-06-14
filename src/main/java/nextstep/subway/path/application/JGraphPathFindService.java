package nextstep.subway.path.application;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class JGraphPathFindService implements PathFindService {

    @Override
    public PathFindResult findShortestPath(WeightedMultigraph<Station, SectionEdge> graph, Station startStation,
                                           Station endStation) throws NotExistPathException {
        ShortestPathAlgorithm<Station, SectionEdge> algorithm = new DijkstraShortestPath<>(graph);

        GraphPath<Station, SectionEdge> shortestPath = algorithm.getPath(startStation, endStation);
        if (shortestPath == null) {
            throw new NotExistPathException("도달가능한 경로가 없습니다.");
        }
        Set<Line> lines = shortestPath.getEdgeList()
                .stream()
                .map(edge -> edge.getLine())
                .collect(toSet());
        return new PathFindResult(shortestPath.getVertexList(), lines, (int) shortestPath.getWeight());
    }
}
