package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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
    private final Path path;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, Path path) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.path = path;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();
        List<Station> shortestPathStations = path.findShortestPathStations(lines, source, target);
        int shortestPathDistance = path.findShortestPathDistance(lines, source, target);
        return PathResponse.of(shortestPathStations, shortestPathDistance);
    }

    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }
}
