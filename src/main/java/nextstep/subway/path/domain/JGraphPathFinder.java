package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.PathResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

@Service
public class JGraphPathFinder implements PathFinder {
    @Override
    public PathResponse getShortestPath(PathRequest pathRequest) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph(pathRequest.getLines()));
        GraphPath<Station, DefaultWeightedEdge> graphPath =
                dijkstraShortestPath.getPath(pathRequest.getSource(), pathRequest.getTarget());
        return new PathResponse(graphPath.getVertexList(), (long) graphPath.getWeight());
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            for (Section section : line.getSections()) {
                Station v1 = section.getUpStation();
                Station v2 = section.getDownStation();
                graph.addVertex(v1);
                graph.addVertex(v2);
                graph.setEdgeWeight(graph.addEdge(v1, v2), section.getDistance());
            }
        }
        return graph;
    }
}
