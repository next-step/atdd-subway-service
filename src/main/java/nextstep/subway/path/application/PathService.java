package nextstep.subway.path.application;

import java.util.NoSuchElementException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository,
        LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(long source, long target) {
        Station sourceStation = stationRepository.findById(source)
            .orElseThrow(() -> new NoSuchElementException("출발역 정보 없음"));
        Station targetStation = stationRepository.findById(target)
            .orElseThrow(() -> new NoSuchElementException("도착역 정보 없음"));

        PathFinder pathFinder = new PathFinder(lineRepository.findAll());
        return PathResponse.from(pathFinder.shortestPath(sourceStation, targetStation),
            pathFinder.getShortestDistance(sourceStation, targetStation));
    }
}
