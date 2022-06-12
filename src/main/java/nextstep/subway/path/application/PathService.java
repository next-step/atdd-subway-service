package nextstep.subway.path.application;

import nextstep.subway.error.ErrorCode;
import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathService(StationService stationService, LineService lineService, PathFinder pathFinder) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        List<Station> stations = stationService.findAll();
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        if (source == target) {
            throw new ErrorCodeException(ErrorCode.SOURCE_EQUALS_TARGET);
        }
        List<Line> lines = lineService.findAll();
        return pathFinder.findPath(stations, lines, source, target);
    }
}
