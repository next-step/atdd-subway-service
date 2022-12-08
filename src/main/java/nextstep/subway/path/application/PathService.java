package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.UserType;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestRoute(Long sourceStationId, Long targetStationId, LoginMember loginMember) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        PathFinder pathFinder = new PathFinder(sourceStation, targetStation, lineService.findLines());

        if (UserType.MEMBER.equals(loginMember.getUserType())) {
            return PathResponse.of(pathFinder,
                    new Fare(pathFinder.getShortestPathDistance(), pathFinder.getExtraFare(), loginMember.getAge()));
        }
        return PathResponse.of(pathFinder, new Fare(pathFinder.getShortestPathDistance(), pathFinder.getExtraFare()));
    }

}
