package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Autowired
    public PathFinder(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        addLineStations(lineRepository.findAll());
        Station srcStation = stationRepository.findById(source).orElseThrow(IllegalAccessError::new);
        Station tgtStation = stationRepository.findById(target).orElseThrow(IllegalAccessError::new);
        return findShortestPath(srcStation, tgtStation);
    }

    public void addLineStation(Line line) {
        line.getSections()
            .getSections()
            .forEach(
                    sec -> {
                        graph.addVertex(sec.getUpStation());
                        graph.addVertex(sec.getDownStation());
                        graph.setEdgeWeight(graph.addEdge(sec.getUpStation(), sec.getDownStation()), sec.getDistance());
                    }
            );
    }

    public void addLineStations(List<Line> lines) {
        lines.forEach(this::addLineStation);
    }

    public PathResponse findShortestPath(Station source, Station target) {
        confirmSelectSameStation(source, target);
        confirmStationIsOnLine(source, target);

        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        GraphPath path = shortestPath.getPath(source, target);
        confirmNonShortestPath(path);
        List<Station> stations = path.getVertexList();
        double distance = shortestPath.getPathWeight(source, target);
        return new PathResponse(stations, distance);
    }

    public void confirmSelectSameStation(Station source, Station target) {
        if (source.getId().equals(target.getId())) {
            throw new RuntimeException("출발역과 도착역이 동일합니다.");
        }
    }

    public void confirmNonShortestPath(GraphPath path) {
        if (path == null) {
            throw new RuntimeException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    public void confirmStationIsOnLine(Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new RuntimeException("존재하지 않은 지하철역을 선택했습니다.");
        }
    }
}
