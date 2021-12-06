package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
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

    public PathResponse getShortestPath(LoginMember loginMember, Long source, Long target) {
        PathFinder pathFinder = new PathFinder(lineService.findAllLines());
        Path path = pathFinder.findShortestPath(findStationById(source), findStationById(target));
        FareCalculator fareCalculator = new FareCalculator(path, loginMember.getAge());
        return PathResponse.of(path, fareCalculator.calculate());
    }

    private Station findStationById(Long stationId) {
        return stationService.findStationById(stationId);
    }
}
