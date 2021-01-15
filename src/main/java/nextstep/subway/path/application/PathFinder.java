package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathVertexStation;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathFinder {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathFinder(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    /**
     * 주어진 출발-도착 역 ID로 지하철역을 찾고, 등록된 전체 구간에서 최단 경로를 구합니다.
     * @param sourceStationId
     * @param targetStationId
     * @return
     */
    public PathResponse getShortestPath(Long sourceStationId, Long targetStationId) {
        Optional<Station> sourceStation = this.stationRepository.findById(sourceStationId);
        Optional<Station> targetStation = this.stationRepository.findById(targetStationId);

        if(sourceStation.isPresent() && targetStation.isPresent()) {
            return this.getShortestPath(sourceStation.get(), targetStation.get());
        }

        throw new IllegalArgumentException("존재하지 않은 지하철역이 있습니다.");
    }

    /**
     * 주어진 출발-도착 역으로 지금까지 등록된 전체 구간에서 최단 경로를 구합니다.
     * @param sourceStation
     * @param targetStation
     * @return 최단 경로
     */
    public PathResponse getShortestPath(Station sourceStation, Station targetStation) {
        this.equalsSourceAndTargetOccurredException(sourceStation, targetStation);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);;

        this.addGraphAllLines(graph);

        GraphPath<Station, DefaultWeightedEdge> shortestPath
                = new DijkstraShortestPath<Station, DefaultWeightedEdge>(graph)
                    .getPath(sourceStation, targetStation);

        return new PathResponse(shortestPath.getVertexList().stream()
                                    .map(PathVertexStation::of).collect(Collectors.toList())
                                , (int) shortestPath.getWeight());
    }

    /**
     * 주어진 두 지하철 역이 같은 경우 최단 경로를 구하지 않고 예외를 발생합니다.
     * @param sourceStation
     * @param targetStation
     */
    private void equalsSourceAndTargetOccurredException(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같은 경우 경로를 구할 수 없습니다.");
        }
    }

    /**
     * 저장된 모든 노선의 구간 정보를 추가합니다.
     * @param graph
     */
    private void addGraphAllLines(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Line> persistLines = lineRepository.findAll();

        persistLines.stream().map(Line::getSections)
                .forEach(sections -> sections
                        .forEach(section -> this.addGraph(graph, section)));
    }

    /**
     * 구간정보와 역 정보를 추가합니다.
     * @param section
     */
    private void addGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        this.addEdgeWeight(graph, section);
    }

    /**
     * 구간 정보를 추가합니다.
     * @param section
     */
    private void addEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation())
                , section.getDistance());
    }
}
