package nextstep.subway.path.domain;

import nextstep.subway.line.dto.PathBag;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph) {
        this.graph = weightedMultigraph;
    }

    public static PathFinder from(WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph) {
        return new PathFinder(weightedMultigraph);
    }

    public PathResult findShortestPath(PathBag pathBag, Station source, Station target) {
        pathBag.findVertex().forEach(graph::addVertex);
        pathBag.getSectionPaths().forEach(it -> graph.setEdgeWeight(
                graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance()));
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return new PathResult(dijkstraShortestPath.getPath(source, target).getVertexList(),
                    dijkstraShortestPath.getPath(source, target).getWeight());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                    "출발역과 도착역 사이에 연결이 끊어져 있습니다. 출발역:" + source.getName() + " 도착역:" + target.getName());
        }
    }
}
