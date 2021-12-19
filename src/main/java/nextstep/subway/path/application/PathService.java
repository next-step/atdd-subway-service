package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        pathFinder.findPath(source, target);
        Map<Long, Station> stationMap = stationRepository.findAllById(pathFinder.getStationIds()).stream()
                .collect(Collectors.toMap(station -> station.getId(), Function.identity()));
        return PathResponse.of(pathFinder.getDistance(), sortStationIds(pathFinder.getStationIds(), stationMap));
    }

    private List<Station> sortStationIds(List<Long> stationIds, Map<Long, Station> stationMap) {
        return stationIds.stream()
                .map(s -> stationMap.get(s))
                .collect(Collectors.toList());
    }
}