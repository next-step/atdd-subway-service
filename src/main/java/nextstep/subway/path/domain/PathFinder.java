package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(lines);
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        return new Path(graphPath.getVertexList(), (int)graphPath.getWeight());
    }

    private void initGraph(List<Line> lines) {
        lines.stream()
            .map(Line::getSections)
            .forEach(sections -> {
                addAllVertex(sections.getStations());
                setEdgeWeight(sections.getSections());
            });
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance())
        );
    }

    private void addAllVertex(Set<Station> stations) {
        stations.forEach(station -> graph.addVertex(station));
    }
}
