package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinderGenerator pathFinderGenerator;

    public PathService(LineService lineService, StationService stationService, PathFinderGenerator pathFinderGenerator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinderGenerator = pathFinderGenerator;
    }

    public PathResponse findPath(Long source, Long target) {
        Lines lines = lineService.findAllLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        PathFinder pathFinder = pathFinderGenerator.generate(lines);
        Path path = pathFinder.findPath(sourceStation, targetStation);

        return PathResponse.from(path);
    }
}
