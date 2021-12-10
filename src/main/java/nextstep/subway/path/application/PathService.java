package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public PathService(StationService stationService, LineService lineService, PathFinder pathFinder, FareCalculator fareCalculator) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse getShortCut(Long sourceStationId, Long targetStationId, LoginMember loginMember) {
        Station source = stationService.findStationById(sourceStationId);
        Station target = stationService.findStationById(targetStationId);

        Set<Line> lines = new HashSet<>(lineService.findLineByStation(source));
        lines.addAll(lineService.findLineByStation(target));

        PathResult shortCut = pathFinder.findShortCut(lines, source, target);
        int fare = fareCalculator.getFare(loginMember, lines, shortCut);

        return PathResponse.of(shortCut, fare);
    }
}
