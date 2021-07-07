package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long source, Long target) {
        checkSameStation(source, target);

        Station sourceStation = findStation(source);
        Station targetStation = findStation(target);
        List<Line> lines = findallLines();
        GraphPath<Station, DefaultWeightedEdge> stationPath = generateShortestPath(sourceStation, targetStation, lines);

        return new PathResponse(stationPath.getVertexList(), (int) stationPath.getWeight());
    }

    public GraphPath<Station, DefaultWeightedEdge> generateShortestPath(Station source, Station target, List<Line> lines) {
        WeightedMultigraph graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setUpGraphByLines(graph, lines);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> stationPath = dijkstraShortestPath.getPath(source, target);
        checkExistPath(stationPath);
        return stationPath;
    }

    private void setUpGraphByLines(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().flatMap(line -> line.getSections().values().stream())
                .forEach(
                        section -> {
                            graph.addVertex(section.getUpStation());
                            graph.addVertex(section.getDownStation());
                            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                        }
        );
    }

    private Station findStation(Long source) {
        return stationRepository.findById(source).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 역입니다."));
    }

    private List<Line> findallLines() {
        return lineRepository.findAll();
    }

    private void checkSameStation(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private void checkExistPath(GraphPath<Station, DefaultWeightedEdge> stationPath) {
        if (stationPath == null) {
            throw new IllegalArgumentException("연결된 경로가 없습니다.");
        }
    }

}
