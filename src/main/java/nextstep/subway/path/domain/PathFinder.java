package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.PathBag;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationLineUp;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;

public class PathFinder {
    private final StationGraph graph;

    public PathFinder() {
        this.graph = new StationGraph(SectionEdge.class);
    }

    public PathResult findShortestPath(PathBag pathBag, Station source, Station target) {
        pathBag.findVertex().forEach(graph::addVertex);
        pathBag.getSectionPaths().forEach(it -> graph.setEdgeWeight(
                graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance()));
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            final List<Station> stations = dijkstraShortestPath.getPath(source, target).getVertexList();
            return new PathResult(
                    stations,
                    new Distance(dijkstraShortestPath.getPath(source, target).getWeight()),
                    pathBag.getMaxLineCharge(new StationLineUp(stations)));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                    "출발역과 도착역 사이에 연결이 끊어져 있습니다. 출발역:" + source.getName() + " 도착역:" + target.getName());
        }
    }
}
