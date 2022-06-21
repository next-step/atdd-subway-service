package nextstep.subway.path.infrastructure;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JGraphPathFinder implements PathFinder {
    @Override
    public ShortestPath getShortestPath(PathRequest pathRequest) {
        Map<DefaultWeightedEdge, Line> edgeLineMap = new HashMap<>();
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph(pathRequest.getLines(), edgeLineMap));
        GraphPath<Station, DefaultWeightedEdge> graphPath =
                dijkstraShortestPath.getPath(pathRequest.getSource(), pathRequest.getTarget());

        return new ShortestPath(graphPath.getVertexList(), new Lines(
                graphPath.getEdgeList().stream().map(edgeLineMap::get).distinct().collect(Collectors.toList())),
                (long) graphPath.getWeight());

    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph(
            Lines lines, Map<DefaultWeightedEdge, Line> weightedEdgeSectionMap) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            for (Section section : line.getSections()) {
                Station v1 = section.getUpStation();
                Station v2 = section.getDownStation();
                graph.addVertex(v1);
                graph.addVertex(v2);
                DefaultWeightedEdge edge = graph.addEdge(v1, v2);
                graph.setEdgeWeight(edge, section.getDistance());
                weightedEdgeSectionMap.put(edge, line);
            }
        }
        return graph;
    }
}
