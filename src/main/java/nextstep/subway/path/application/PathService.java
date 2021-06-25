package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Graph;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final Graph graph;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, Graph graph) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graph = graph;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();
        graph.build(lines);
        Path path = graph.findShortestPath(source, target);
        return PathResponse.of(path.getStations(), path.getDistance());
    }

    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }
}
