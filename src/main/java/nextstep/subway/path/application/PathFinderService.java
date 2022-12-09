package nextstep.subway.path.application;


import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.path.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.path.domain.fare.FareStrategy;
import nextstep.subway.path.domain.path.Path;
import nextstep.subway.path.domain.path.PathFinder;
import nextstep.subway.path.domain.path.SectionEdges;
import nextstep.subway.path.domain.path.StationGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

@Service
public class PathFinderService {

    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public PathFinderService(SectionRepository sectionRepository, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathResponse getShortestPath(Long sourceId, Long targetId, LoginMember loginMember) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        StationGraph stationGraph = StationGraph.of(sectionRepository.findAll());
        PathFinder pathFinder = DijkstraShortestPathFinder.of(stationGraph);
        GraphPath graphPath = pathFinder.findPath(source, target);
        FareStrategy fareStrategy = createFareStrategy(graphPath, loginMember);
        Path path = Path.of(graphPath, Fare.of(fareStrategy));
        return PathResponse.from(path);
    }
    private FareStrategy createFareStrategy(GraphPath graphPath, LoginMember loginMember){
        SectionEdges sectionEdges = SectionEdges.of(graphPath.getEdgeList());
        Distance distance = Distance.from(graphPath.getWeight());
        return FareStrategy.of(sectionEdges, distance, loginMember);
    }
}
