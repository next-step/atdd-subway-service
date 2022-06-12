package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Lines lines, Station upStation, Station downStation) {
        addVertex(lines);
        assignEdgeWeight(lines);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(upStation, downStation);
    }

    private void addVertex(Lines lines) {
        for (Station station : lines.getAllStations()) {
            graph.addVertex(station);
        }
    }

    private void assignEdgeWeight(Lines lines) {
        for (Section section : lines.getAllSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
