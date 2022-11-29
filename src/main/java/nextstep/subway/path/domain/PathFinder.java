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

    public PathFinder() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public Path findShortestPath(List<Line> lines, Station source, Station target) {
        addStationsInGraph(lines);
        addSectionsInGraph(lines);

        return createPath(source, target);
    }

    private Path createPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
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
