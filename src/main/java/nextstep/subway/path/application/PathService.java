package nextstep.subway.path.application;

import java.util.List;
import java.util.Optional;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Integer age, Long source, Long target) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = DijkstraShortestPathFinder.from(lines);
        Path path = pathFinder.findPath(sourceStation, targetStation);
        return PathResponse.of(path.getStations(), path.getDistance(), path.calculateFare(Optional.ofNullable(age).orElse(0)));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(SubwayException::new);
    }
}
