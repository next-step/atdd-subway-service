package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = findStationById(sourceStationId);
        Station targetStation = findStationById(targetStationId);

        GraphPath<Station, DefaultWeightedEdge> path = getShortestPath(lines, sourceStation,
            targetStation);
        List<Station> shortestPath = path.getVertexList();

        return new PathResponse(StationResponse.of(shortestPath), (int) path.getWeight());
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(List<Line> lines,
        Station sourceStation, Station targetStation) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);

        for (Line line : lines) {
            addStationsToVertex(graph, line);
            addSectionsToEdgeWithWeight(graph, line);
        }

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

    private void addSectionsToEdgeWithWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Line line) {
        for (Section section : line.getSections().getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
    }

    private void addStationsToVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("id에 해당하는 역이 없습니다."));
    }

}
