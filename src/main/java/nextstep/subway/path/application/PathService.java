package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();

        Station sourceStation = findStation(pathRequest.getSource());
        Station targetStation = findStation(pathRequest.getTarget());

        validateSourceAndTargetStation(sourceStation, targetStation);

        GraphPath<Station, DefaultWeightedEdge> graph = findShortestPath(lines, sourceStation, targetStation);
        List<StationResponse> paths = graph.getVertexList().stream()
                .map(i -> new StationResponse(i.getId(), i.getName(), i.getCreatedDate(), i.getModifiedDate()))
                .collect(Collectors.toList());

        return new PathResponse(paths, (int) graph.getWeight());
    }

    private void validateSourceAndTargetStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createGraphFromLines(lines);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        try {
            return dijkstraShortestPath.getPath(sourceStation, targetStation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역과 도착역이 이어진 경로가 없습니다.");
        }
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraphFromLines(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addStationsToVertex(graph, line);
            addSectionsToEdgeWithWeight(graph, line);
        }

        return graph;
    }

    private void addStationsToVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void addSectionsToEdgeWithWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
