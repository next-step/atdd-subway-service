package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    private PathFinder(List<Station> stations, List<Section> sections) {
        makeGraph(stations, sections);
    }

    public static PathFinder of(List<Station> stations, List<Section> sections) {
        return new PathFinder(stations, sections);
    }

    public Path findPath(Station source, Station target){
        validFindPaths(source, target);
        return Path.from(findShortestPath(source, target));
    }

    private void makeGraph(List<Station> stations, List<Section> sections) {
        stations.forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance()));
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(source, target);
        validPathFindResult(shortestPath);
        return shortestPath;
    }

    private void validPathFindResult(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("연결되지 않은 역 입니다.");
        }
    }

    private void validFindPaths(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("동일한 역으로 경로조회할 수 없습니다.");
        }
    }
}
