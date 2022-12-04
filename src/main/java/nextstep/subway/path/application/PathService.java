package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target, int age) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        PathFinder pathFinder = PathFinder.from(lineRepository.findAll());
        Path shortestPath = pathFinder.findShortestPath(sourceStation, targetStation, age);
        return PathResponse.from(shortestPath);
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
