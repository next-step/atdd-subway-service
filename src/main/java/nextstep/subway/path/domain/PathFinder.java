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
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder() {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(List<Line> lines, Station upStation, Station downStation) {
        for (Line line : lines) {
            List<Station> stations = line.getStations();
            for (Station station : stations) {
                graph.addVertex(station);
            }

            List<Section> sections = line.getSections();
            for (Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            }
        }

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(upStation, downStation);
    }
}
