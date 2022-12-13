package nextstep.subway.line.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PathService {
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<StationResponse> getShortestPath(final String sourceStationId, final String targetStationid) {
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();
        Station sourceStation = stationRepository.getById(Long.valueOf(sourceStationId));
        Station targetStation = stationRepository.getById(Long.valueOf(targetStationid));
        List<String> shortestPath = getShortestPath(sections, stations)
                            .getPath(sourceStation.getName(), targetStation.getName())
                            .getVertexList();
        return getShortestPathResponse(stations, shortestPath);
    }

    private List<StationResponse> getShortestPathResponse(List<Station> stations, List<String> shortestPath) {
        List<StationResponse> shortestStations = new ArrayList<>();

        for (String stationName : shortestPath) {
            Optional<Station> station = stations.stream()
                    .filter(it -> it.getName().equals(stationName))
                    .findFirst();

            station.ifPresent(it -> shortestStations.add(StationResponse.of(it)));
        }

        return shortestStations;
    }

    private DijkstraShortestPath<String, DefaultWeightedEdge> getShortestPath(List<Section> sections, List<Station> stations) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        setGraphVertex(stations, graph);
        setGraphEdge(sections, graph);

        return new DijkstraShortestPath<>(graph);
    }

    private void setGraphEdge(List<Section> sections, WeightedMultigraph graph) {
        sections.stream()
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName()
                                                                    , section.getDownStation().getName())
                                                                    , section.getDistance()));
    }

    private void setGraphVertex(List<Station> stations, WeightedMultigraph graph) {
        stations.stream()
                .forEach(station ->  graph.addVertex(station.getName()));
    }
}
