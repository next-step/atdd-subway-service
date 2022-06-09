package nextstep.subway.path.application;

import nextstep.subway.fare.calculator.FareCalculator;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortPath(Long sourceId, Long targetId) {
        Lines lines = lineService.findAll();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(source, target);
        return PathResponse.of(path, findPathFare(lines, path));
    }

    private int findPathFare(Lines lines, Path path) {
        return FareCalculator.calculate(lines, path);
    }
}
