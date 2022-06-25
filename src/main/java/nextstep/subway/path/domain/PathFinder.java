package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public PathFinder(List<Line> lines) {
        lines.forEach(this::addGraph);
    }

    private void addGraph(Line line) {
        addVertex(line.getStations());
        setEdgeWeight(line.getSections());
    }

    private void addVertex(List<Station> stations) {
        stations.forEach(station -> graph.addVertex(station.getId()));
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance()));
    }

    public GraphPath<Long, DefaultWeightedEdge> getPaths(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source.getId(), target.getId());
    }

    public Long getDistance(GraphPath<Long, DefaultWeightedEdge> graphPath) {
        return (long) graphPath.getWeight();
    }
}
