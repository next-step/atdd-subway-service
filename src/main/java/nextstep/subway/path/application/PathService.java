package nextstep.subway.path.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = DijkstraShortestPathFinder.from(lines);
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);

        return PathResponse.from(path);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.STATION_NOT_EXIST));
    }
}
