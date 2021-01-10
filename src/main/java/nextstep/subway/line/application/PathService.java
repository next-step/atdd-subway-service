package nextstep.subway.line.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPaths(Long source, Long target) {

        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Section> sections = lineService.findAllSections();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Set<Station> stationSet = new HashSet<>();
        sections.forEach(section -> {
            stationSet.add(section.getUpStation());
            stationSet.add(section.getDownStation());
        });
        stationSet.forEach(graph::addVertex);

        sections.forEach(section ->
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath shortPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        List<Station> shortestPath = shortPath.getVertexList();
        double shortestWeight = shortPath.getWeight();

        return PathResponse.of(shortestPath, shortestWeight);
    }
}
