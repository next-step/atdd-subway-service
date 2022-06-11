package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.ServiceMember;
import nextstep.subway.fare.calculator.FareCalculator;
import nextstep.subway.fare.domain.DiscountAgeRule;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
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

    public PathResponse findShortPath(ServiceMember serviceMember, Long sourceId, Long targetId) {
        Lines lines = lineService.findAll();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(source, target);
        return PathResponse.of(path, calculatePathFare(serviceMember, lines, path));
    }

    private int calculatePathFare(ServiceMember serviceMember, Lines lines, Path path) {
        int fare = FareCalculator.calculate(lines, path);

        if (serviceMember instanceof LoginMember) {
            fare = DiscountAgeRule.discountFare(fare, serviceMember.getAge());
        }

        return fare;
    }
}
