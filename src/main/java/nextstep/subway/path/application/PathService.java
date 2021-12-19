package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        Station sourceStation = stationService.findStationById(pathRequest.getSource());
        Station targetStation = stationService.findStationById(pathRequest.getTarget());

        Lines lines = lineService.findLines();
        PathFinder pathFinder = PathFinder.from(lines);
        Path path = pathFinder.findShortestPath(lines, sourceStation, targetStation);
        Fare fare = getFare(loginMember, path);
        return PathResponse.of(path, fare);
    }

    private Fare getFare(LoginMember loginMember, Path path) {
        FarePolicy farePolicy = FarePolicy.of(loginMember, path);
        farePolicy.calculateMaxExtraFare();
        farePolicy.calculatorExtraFareByDistance();
        farePolicy.calculateAgeDiscount();
        return farePolicy.getFare();
    }
}
