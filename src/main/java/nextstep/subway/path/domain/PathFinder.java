package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PathFinder {

    private List<Line> lines;
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(List<Line> lines, WeightedMultigraph graph) {
        this.lines = lines;
        this.graph = graph;
    }

    public static PathFinder of(List<Line> lines) {
        PathFinder pathFinder = new PathFinder(lines, new WeightedMultigraph(DefaultWeightedEdge.class));
        pathFinder.initGraph();
        return pathFinder;
    }

    private void initGraph() {

        List<Station> stations = this.lines.stream()
                .flatMap(l -> l.getStationsByOrder().stream())
                .collect(toList());

        stations.forEach(s -> graph.addVertex(s));

        List<Section> sections = this.lines.stream()
                .flatMap(l -> l.getSections().stream())
                .collect(toList());

        sections.forEach(s -> graph.setEdgeWeight(graph.addEdge(s.getUpStation(), s.getDownStation()), s.getDistance().getValue()));
    }

    public PathResult find(Station source, Station target) {

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(this.graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        List<Station> shortest = path.getVertexList();
        double weight = path.getWeight();

        return new PathResult(shortest, weight);
    }
}
