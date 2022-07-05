package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.User;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.util.FareCalculator;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final LineRepository lineRepository;
    private PathFinder pathFinder;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        pathFinder = new PathFinder();
    }

    public PathResponse findShortestPath(User user, Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Section> allSection = lineRepository.findAll().stream()
            .map(line -> line.getSections())
            .flatMap(sections -> sections.stream())
            .collect(Collectors.toList());
        GraphPath shortestPath = pathFinder.findShortestPath(allSection, sourceStation, targetStation);
        Fare fare = findShortestPathFare(shortestPath, user);
        return new PathResponse(shortestPath.getVertexList(), (long) shortestPath.getWeight(), fare);
    }

    private Fare findShortestPathFare(GraphPath shortestPath, User user) {
        Fare maxLineFare = pathFinder.findMaxLineFare(shortestPath);
        Fare calculateFare = FareCalculator.calculateFare((long) shortestPath.getWeight(), maxLineFare.value());
        return FareCalculator.calculateFareWithMemberType(user, calculateFare.value());
    }
}
