package nextstep.subway.path.application;

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

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }
    public PathResponse findPath(Long sourceId, Long targetId) {
        List<Line> lines = lineService.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<Station> stations = stationService.findAllByIdIn(pathFinder.getStationsId(source, target));
        Long distance = pathFinder.getDistance(source, target);
        return PathResponse.of(stations, distance);
    }
}
