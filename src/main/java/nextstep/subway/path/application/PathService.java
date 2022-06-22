package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.AgeDiscountFare;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
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
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId, int age) {
        List<Line> lines = lineRepository.findAll();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        PathFinder pathFinder = new PathFinder(new DijkstraShortestPathFinder());
        Path path = pathFinder.findShortestPath(lines, source, target);
        int fare = AgeDiscountFare.calculate(path.getFare(), age);

        return PathResponse.of(path, fare);
    }
}
