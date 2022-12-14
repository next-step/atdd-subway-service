package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    public PathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertx(line);
            setEdgeWeight(line.getSections());
        });
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance()
                ));
    }

    private void addVertx(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    public Path findShortPath(Station startStation, Station endStation) {

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, endStation);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }
}
