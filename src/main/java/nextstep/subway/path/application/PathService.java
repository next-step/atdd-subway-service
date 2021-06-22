package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();
        Station source = findStation(pathRequest.getSource());
        Station target = findStation(pathRequest.getTarget());
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.stream()
             .flatMap(line -> line.getStations().stream())
             .collect(Collectors.toSet())
             .forEach(graph::addVertex);

        lines.stream()
             .flatMap(line -> line.getSections().stream())
             .forEach(section ->
                 graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
             );
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        return PathResponse.of(dijkstraShortestPath.getPath(source, target).getVertexList(),
                (int)dijkstraShortestPath.getPathWeight(source, target));
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
