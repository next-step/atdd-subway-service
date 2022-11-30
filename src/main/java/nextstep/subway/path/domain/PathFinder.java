package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        addStationsInGraph(lines);
        addSectionsInGraph(lines);

        validateSourceAndTarget(source, target);

        return createPath(source, target);
    }

    private void validateSourceAndTarget(Station source, Station target) {
        ifSameStationThenThrow(source, target);
        ifNotExistEdgeThenThrow(source, target);
    }

    private void ifNotExistEdgeThenThrow(Station source, Station target) {
        try {
            dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 도착역의 연결정보가 없습니다.");
        }
    }

    private void ifSameStationThenThrow(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private Path createPath(Station source, Station target) {
        List stations = dijkstraShortestPath.getPath(source, target).getVertexList();
        double weight = dijkstraShortestPath.getPath(source, target).getWeight();
        return new Path(stations, (int) weight);
    }

    private void addSectionsInGraph(List<Line> lines) {
        lines.forEach(line -> line.getSections()
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance())));
    }

    private void addStationsInGraph(List<Line> lines) {
        Set<Station> stations = new HashSet<>();
        lines.forEach(line -> stations.addAll(line.getStations()));
        stations.forEach(station -> graph.addVertex(station));
    }
}
