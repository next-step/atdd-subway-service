package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.exception.StationException;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.line.application.LineService.STATION_NOT_FOUND_MESSAGE;


@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPaths(long source, long target) {
        Station startStation = findStationById(source);
        Station endStation = findStationById(target);
        List<Line> lines = findLines();
        DijkstraPathFinder dijkstraPathFinder = DijkstraPathFinder.ofList(lines);
        Path shortestPath = dijkstraPathFinder.findPath(startStation, endStation);
        return PathResponse.from(shortestPath);
    }

    @Transactional(readOnly = true)
    public Station findStationById(long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationException(STATION_NOT_FOUND_MESSAGE));
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineRepository.findAll();
    }
}
