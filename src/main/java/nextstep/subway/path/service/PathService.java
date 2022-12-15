package nextstep.subway.path.service;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
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

    public PathResponse findShortPath(long startStationId, long endStationId) {
        Station startStation = findStationById(startStationId);
        Station endStation = findStationById(endStationId);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findShortPath(startStation, endStation);

        return PathResponse.from(path);
    }

    private Station findStationById(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.EMPTY_STATIONS.getMessage()));
    }

}
