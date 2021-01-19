package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PathFindService {
    private final LineService lineService;
    private final StationService stationService;

    public PathFindService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station start = stationService.findById(source);
        Station end = stationService.findById(target);

        Set<Station> stations = lineService.getAllStations();
        List<Section> sections = lineService.getAllSections();

        PathFinder pathFinder = new PathFinder(stations, sections);
        ShortestPathInfo shortestPath = pathFinder.findShortestPath(start, end);

        return new PathResponse(shortestPath.getResultStations(), shortestPath.getShortestDistance());
    }
}
