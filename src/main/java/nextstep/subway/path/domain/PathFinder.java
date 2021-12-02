package nextstep.subway.path.domain;

import nextstep.subway.fee.AdditionalFeeByDistance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, LineWeightedEdge> graph;
    private final DijkstraShortestPath<Station, LineWeightedEdge> dijkstraShortestPath;

    private PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(LineWeightedEdge.class);
        createPath(lines);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(lines);
    }

    public Path getShortestPath(Station source, Station target, int age) {

        GraphPath<Station, LineWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        int fee = AdditionalFeeByDistance.getFee(path);
        return new Path(new Stations(path.getVertexList()),(int) path.getWeight(), fee, age);
    }

    private void createPath(List<Line> lines) {
        lines.forEach(
                line -> line.getStations()
                            .forEach(graph::addVertex));
        lines.forEach(
                line -> line.getSections()
                            .forEach(section -> addEdge(line, section)));
    }

    private void addEdge(Line line, Section section) {
        LineWeightedEdge lineWeightedEdge = new LineWeightedEdge(line);
        this.graph.addEdge(section.getUpStation(), section.getDownStation(), lineWeightedEdge);
        this.graph.setEdgeWeight(lineWeightedEdge, section.getDistance() );
    }

    @Override
    public String toString() {
        return "PathFinder{" +
                "graph=" + graph +
                ", dijkstraShortestPath=" + dijkstraShortestPath +
                '}';
    }
}
