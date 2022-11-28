package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    private PathFinder(Lines lines) {
        addVertex(lines.stations());
        setEdgeWeight(lines.sections());
        path = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder from(Lines lines) {
        return new PathFinder(lines);
    }

    private void addVertex(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(addEdge(section), section.distanceValue()));
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.upStation(), section.downStation());
    }

    public Path findShortestPath(Station departure, Station arrival) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = path.getPath(departure, arrival);
        return Path.of(shortestPath.getVertexList(), Distance.from((int) shortestPath.getWeight()));
    }
}
