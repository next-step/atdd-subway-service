package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPathByIds(Long sourceId, Long targetId) {
        if(sourceId == targetId) {
            throw new IllegalArgumentException();
        }
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.initialPathFinder(lines);
        GraphPath path = pathFinder.getShortestPath(sourceId, targetId);
        if(Objects.isNull(path)) {
            throw new IllegalArgumentException();
        }
        return PathResponse.of(getStationsById(path.getVertexList()), path.getWeight());
    }

    public List<Station> getStationsById(List<Long> ids) {
        return ids.stream()
                .map(id -> stationRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());
    }
}
