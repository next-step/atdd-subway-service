package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(List<Section> allSections) {
        this.graph = makeGraph(allSections);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(final List<Section> allSections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        allSections.forEach(
                section -> {
                    graph.addVertex(section.getUpStation());
                    graph.addVertex(section.getDownStation());
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                }
        );
        return graph;
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> stations = dijkstraShortestPath.getPath(source, target).getVertexList();
        int distance = (int) dijkstraShortestPath.getPath(source, target).getWeight();
        return new Path(stations, distance);
    }

    public static PathFinder of(List<Section> allSections) {
        return new PathFinder(allSections);
    }
}
