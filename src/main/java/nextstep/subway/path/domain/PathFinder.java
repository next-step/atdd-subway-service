package nextstep.subway.path.domain;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {

    private final StationService stationService;
    private final LineService lineService;

    public PathFinder(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public DijkstraShortestPath findDijkstraPath() {
        List<Station> stations = stationService.findAll();
        List<Section> sections = lineService.findAllSection();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new DijkstraShortestPath(graph);
    }

    public List<Station> findShortestPathStations(DijkstraShortestPath shortestPath, Station startStation, Station endStation) {
      return shortestPath.getPath(startStation, endStation).getVertexList();
    }

    public int findShortestPathDistance(DijkstraShortestPath shortestPath, Station startStation, Station endStation) {
        return (int) shortestPath.getPathWeight(startStation, endStation);
    }



}
