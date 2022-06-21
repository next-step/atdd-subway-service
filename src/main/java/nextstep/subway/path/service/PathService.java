package nextstep.subway.path.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse getPath(PathRequest request, LoginMember loginMember) {
        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());

        PathFinder pathFinder = new PathFinder(lineService.findAllLines());
        Path path = pathFinder.findPath(sourceStation, targetStation);
        Fare fare = new Fare(path.getDistance(), loginMember.getAge(), path.getMaxLineExtraFare());

        return PathResponse.of(path.getStations(), path.getDistance(), fare);
    }
}
