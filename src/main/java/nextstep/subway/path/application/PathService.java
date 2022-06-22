package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(long sourceId, long targetId) {
        final Station sourceStation = stationService.findStationById(sourceId);
        final Station targetStation = stationService.findStationById(targetId);
        final PathFinder pathFinder = new PathFinder(lineService.findAllLines());

        return PathResponse.of(pathFinder.find(sourceStation, targetStation));
    }
}
