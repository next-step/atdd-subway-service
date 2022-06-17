package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public PathFinder() {
    }

    public void addLineStation(Line line) {
        line.getSections()
            .getSections()
            .forEach(
                    sec -> {
                        graph.addVertex(sec.getUpStation());
                        graph.addVertex(sec.getDownStation());
                        graph.setEdgeWeight(graph.addEdge(sec.getUpStation(), sec.getDownStation()), sec.getDistance());
                    }
            );
    }

    public PathResponse findShortestPath(Station source, Station target) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        List<Station> stations = shortestPath.getPath(source, target).getVertexList();
        double distance = shortestPath.getPathWeight(source, target);
        return new PathResponse(stations, distance);
    }
}
