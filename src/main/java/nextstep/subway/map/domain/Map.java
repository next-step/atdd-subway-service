package nextstep.subway.map.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Map {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    private Map(Sections sections) {
        for (Station station : sections.getStations().getStations()) {
            graph.addVertex(station);
        }
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public static Map of(List<Section> sections) {
        return new Map(Sections.of(sections));
    }

    public PathResponse findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        Stations stations = Stations.of(path.getVertexList());
        int distance = (int) path.getWeight();

        return PathResponse.of(stations.toResponse(), distance);
    }
}
