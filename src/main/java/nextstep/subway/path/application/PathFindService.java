package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.FareCalculator;
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

    public PathFindService(LineService lineService,
                           StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPathAndFare(LoginMember loginMember, Long source, Long target) {
        ShortestPathInfo shortestPath = findShortestPath(source, target);
        Integer distance = shortestPath.getShortestDistance();
        List<Station> pathStations = shortestPath.getResultStations();

        Integer fare = calculateFare(loginMember, pathStations, distance);
        return new PathResponse(pathStations, distance, fare);
    }

    private ShortestPathInfo findShortestPath(Long source, Long target) {
        Station start = stationService.findById(source);
        Station end = stationService.findById(target);

        Set<Station> stations = lineService.getAllStations();
        List<Section> sections = lineService.getAllSections();

        PathFinder pathFinder = new PathFinder(stations, sections);
        return pathFinder.findShortestPath(start, end);
    }

    private Integer calculateFare(LoginMember loginMember, List<Station> pathStations, Integer distance) {
        int lineExtraFare = findLineExtraFare(pathStations);
        return FareCalculator.calculateFare(lineExtraFare, distance, loginMember);
    }

    private int findLineExtraFare(List<Station> pathStations) {
        return lineService.getMaxExtraLineFare(pathStations);
    }
}
