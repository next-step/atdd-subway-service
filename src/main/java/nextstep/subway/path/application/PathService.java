package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findBestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);
        List<Line> lines = findAllLines();

        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findShortestPath(source, target);

        Fare fare = new Fare(path.getDistance(), path.getMaxSurcharge(), loginMember.getAge());
        return new PathResponse(path.getStations(), path.getDistance(), fare);
    }

    private Station findStationById(Long stationId) {
        return stationService.findById(stationId);
    }

    private List<Line> findAllLines() {
        return lineService.findAllLines();
    }
}