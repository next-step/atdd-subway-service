package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Station> findResult = findAllByIdIn(pathRequest);

        Station source = findResult.stream()
                .filter(station -> station.isSameStation(pathRequest.getSourceStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));

        Station target = findResult.stream()
                .filter(station -> station.getId().equals(pathRequest.getTargetStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도착역입니다."));

        List<Line> lines = lineRepository.findAll();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> initGraph(graph, line));

        GraphPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph).getPath(source, target);
        return PathResponse.of(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private void initGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSections().forEach(section -> {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistanceWeight());
        });
    }

    private List<Station> findAllByIdIn(PathRequest pathRequest) {
        return stationRepository.findAllByIdIn(
                Arrays.asList(pathRequest.getSourceStationId(), pathRequest.getTargetStationId()));
    }
}
