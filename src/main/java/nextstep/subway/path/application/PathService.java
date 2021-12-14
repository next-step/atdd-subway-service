package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public <T> PathResponse findPath(T member, Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.shortestPath(source, target);
        path.calculateFare(member, lines);
        return PathResponse.ofList(path);
    }
}
