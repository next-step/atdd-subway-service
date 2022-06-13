package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public PathService(StationService stationService, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }

        Station sourceStation = this.stationService.findStationById(source);
        Station targetStation = this.stationService.findStationById(target);

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<Section> sections = this.sectionRepository.findAll();
        List<StationResponse> stations = this.stationService.findAllStations();

        stations.forEach(stationResponse -> graph.addVertex(stationResponse.getId().toString()));
        sections.forEach(section -> graph.setEdgeWeight(
            graph.addEdge(section.getUpStation().getId().toString(), section.getDownStation().getId().toString()),
            section.getDistance().getValue()
        ));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath shortestPath = dijkstraShortestPath.getPath(sourceStation.getId().toString(), targetStation.getId().toString());
        if (shortestPath == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
        List<String> vertexList = shortestPath.getVertexList();
        List<Station> resultStationList = vertexList.stream().map(id -> this.stationService.findStationById(Long.valueOf(id)))
            .collect(Collectors.toList());
        double weight = shortestPath.getWeight();

        return new PathResponse(resultStationList, weight);
    }

}
