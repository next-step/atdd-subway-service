package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private StationService stationService;
    private LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPaths(Long sourceId, Long targetId) {
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);
        List<Line> lines = lineService.findLines();
        Path path = PathFinder.of(lines).findPathBetweenStations(sourceStation, targetStation);
        return PathResponse.of(path);
    }
}
