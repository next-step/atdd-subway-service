package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.SameOriginDestinationException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        if (source.equals(target)) {
            throw new SameOriginDestinationException("출발지와 도착지가 같습니다.");
        }
        List<Line> lines = lineService.findAll();

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        Path shortestPath = pathFinder.findShortest(lines, sourceStation, targetStation);

        if (loginMember.isLogin()) {
            shortestPath.discountFareByAge(loginMember.getAge());
        }

        return PathResponse.of(shortestPath);
    }
}
