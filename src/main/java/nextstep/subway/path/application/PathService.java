package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.JGraphPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathService(StationService stationService,
        LineService lineService, PathFinder pathFinder) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse searchShortestPath(Long sourceStationId, Long targetStationId) {
        List<Section> sections = lineService.findAllSections();
        Station source = stationService.findStationById(sourceStationId);
        Station target = stationService.findStationById(targetStationId);

        pathFinder.determinePathFindStrategy(new JGraphPathFinder());

        PathResponse pathResponse = pathFinder.searchShortestPath(sections, source, target);
        return pathResponse;
    }

}
