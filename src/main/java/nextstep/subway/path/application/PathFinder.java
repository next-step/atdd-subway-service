package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class PathFinder {

    public static final String MESSAGE_STATIONS_NOT_ABLE_TO_REACHED = "출발역과 도작역이 연결되어 있지 않습니다";
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

    public List<Station> findByDijkstra(Station departStation, Station destStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(departStation, destStation);
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException(MESSAGE_STATIONS_NOT_ABLE_TO_REACHED);
        }
        return path.getVertexList();
    }

    public List<Station> findByKShortest(Station departStation, Station destStation) {
        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(departStation, destStation);
        if(CollectionUtils.isEmpty(paths)){
            throw new IllegalArgumentException(MESSAGE_STATIONS_NOT_ABLE_TO_REACHED);
        }
        return paths.get(0).getVertexList();
    }

    public static PathFinder of(List<Section> allSections) {
        return new PathFinder(allSections);
    }
}
