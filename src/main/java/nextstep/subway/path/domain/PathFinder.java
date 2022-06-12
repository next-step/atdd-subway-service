package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private DijkstraShortestPath shortestPath;

    public PathFinder(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            Sections sections = line.getSections();
            for (Station station : sections.getStations()) {
                graph.addVertex(station);
            }
        }

        for (Line line : lines) {
            Sections sections = line.getSections();
            for (Section section : sections.getList()) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance());
            }
        }
        shortestPath = new DijkstraShortestPath(graph);
    }
}
