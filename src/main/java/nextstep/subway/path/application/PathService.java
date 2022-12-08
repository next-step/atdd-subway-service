package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.LoginMemberThreadLocal;
import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.path.fare.discount.AgeDiscount;
import nextstep.subway.path.fare.extra.ExtraFare;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineQueryService lineQueryService;

    public PathService(StationService stationService, LineQueryService lineQueryService) {
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        PathFinder pathFinder = createPathFinder();

        Path path = pathFinder.find(sourceStation, targetStation);
        Fare fare = calculateFare(path);

        return new PathResponse(path.getStations(), path.getDistance(), fare);
    }

    private PathFinder createPathFinder() {
        return new PathFinder(lineQueryService.getAllLines());
    }

    private Fare calculateFare(Path path) {
        Fare extraFare = ExtraFare.calculateExtraFare(path);
        return calculateDiscountFare(extraFare);
    }

    private Fare calculateDiscountFare(Fare extraFare) {
        LoginMember loginMember = getLoginMember();
        return AgeDiscount.discountFare(loginMember, extraFare);
    }

    private LoginMember getLoginMember() {
        return LoginMemberThreadLocal.get();
    }
}
