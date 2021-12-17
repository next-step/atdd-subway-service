package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.AgeDiscount;
import nextstep.subway.path.infra.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, StationService stationService, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(long source, long target, LoginMember loginMember) {

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        List<Line> lines = lineRepository.findAll();

        ShortestPath shortestPath = pathFinder.findShortestPath(lines, sourceStation, targetStation);
        Fare fare = shortestPath.findFare().plusLineUseFare(lines);
        if (!loginMember.isEmpty()) {
            fare = AgeDiscount.discount(loginMember.getAge(), fare);
        }
        return new PathResponse(shortestPath.findPaths(), shortestPath.findDistance(), fare);
    }
}
