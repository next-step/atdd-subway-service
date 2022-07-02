package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.MemberType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
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

    public PathResponse findShortestPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Section> allSection = lineRepository.findAll().stream()
            .map(line -> line.getSections())
            .flatMap(sections -> sections.stream())
            .collect(Collectors.toList());
        GraphPath shortestPath = pathFinder.findShortestPath(allSection, sourceStation, targetStation);
        Fare fare = findShortestPathFare(shortestPath, loginMember.findMemberType());
        return new PathResponse(shortestPath.getVertexList(), (long) shortestPath.getWeight(), fare);
    }

    private Fare findShortestPathFare(GraphPath shortestPath, MemberType memberType) {
        Fare maxLineFare = pathFinder.findMaxLineFare(shortestPath);
        Fare calculateFare = maxLineFare.calculateFare((long) shortestPath.getWeight());
        return calculateFare.calculateFareWithMemberType(memberType);
    }
}
