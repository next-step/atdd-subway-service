package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Objects;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Lines lines, Station upStation, Station downStation) {
        validateVertex(upStation, downStation);
        addVertex(lines);
        assignEdgeWeight(lines);

        return getPath(upStation, downStation);
    }

    private void validateVertex(Station upStation, Station downStation) {
        Objects.requireNonNull(upStation, "역이 없습니다.");
        Objects.requireNonNull(downStation, "역이 없습니다.");

        if (upStation.match(downStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
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

    private GraphPath<Station, DefaultWeightedEdge> getPath(Station upStation, Station downStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(upStation, downStation);
    }
}
