package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Station> stations, List<Section> sections) {
        this.graph = createGraph(stations, sections);
    }

    public DijkstraShortestPath findDijkstraPath() {
       return new DijkstraShortestPath(graph);
    }

    public List<Station> findShortestPathStations(DijkstraShortestPath shortestPath, Station startStation, Station endStation) {
        return shortestPath.getPath(startStation, endStation).getVertexList();
    }

    public int findShortestPathDistance(DijkstraShortestPath shortestPath, Station startStation, Station endStation) {
        return (int) shortestPath.getPathWeight(startStation, endStation);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return graph;
    }

}
