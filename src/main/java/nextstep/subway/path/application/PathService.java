package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.Path;
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

    public PathResponse searchShortestPath(LoginMember loginMember, Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        final Path path = Path.findShortestPath(lineService.findAllLines(), sourceStation, targetStation);
        path.discountFee(loginMember);
        return path.toPathResponse();
    }

    public void validatePath(Station source, Station target) {
        final PathFinder pathFinder = PathFinder.create();
        pathFinder.init(lineService.findAllLines());
        pathFinder.validatePath(source, target);
    }
}
