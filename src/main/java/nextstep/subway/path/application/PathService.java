package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);
        Sections sections = lineService.getSections();
        return PathResponse.of(pathFinder.findShortestPath(sections, sourceStation, targetStation));
    }
}
