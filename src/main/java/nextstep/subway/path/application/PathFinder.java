package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private final List<Section> allSections;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(List<Section> allSections) {
        this.allSections = allSections;
        this.graph = makeGraph();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.allSections.forEach(
                section -> {
                    graph.addVertex(section.getUpStation());
                    graph.addVertex(section.getDownStation());
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(),section.getDownStation()),section.getDistance());
                }
        );
        return graph;
    }

    public List<Station> findByDijkstra(Station departStation, Station destStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(departStation, destStation).getVertexList();
    }

    public List<Station> findByKShortest(Station departStation, Station destStation) {
        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(departStation, destStation);
        if(CollectionUtils.isEmpty(paths)){
            return new ArrayList<>();
        }
        return paths.get(0).getVertexList();
    }

    public static PathFinder of(List<Section> allSections) {
        return new PathFinder(allSections);
    }
}
