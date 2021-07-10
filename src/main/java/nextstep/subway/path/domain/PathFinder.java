package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Station> stations, List<Section> sections) {
        this.dijkstraShortestPath = createPath(stations, sections);
    }

    public List<Station> findShortestPathStations(Station startStation, Station endStation) {
        GraphPath shortPath = this.dijkstraShortestPath.getPath(startStation, endStation);

        if (shortPath == null) {
            throw new IllegalArgumentException(startStation + "와 " + endStation + "가 연결되어 있지 않습니다.");
        }

        return shortPath.getVertexList();
    }

    public int findShortestPathDistance(Station startStation, Station endStation) {
        return (int) this.dijkstraShortestPath.getPathWeight(startStation, endStation);
    }

    private DijkstraShortestPath createPath(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new DijkstraShortestPath(graph);
    }
}
