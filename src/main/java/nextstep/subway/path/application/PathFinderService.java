package nextstep.subway.path.application;


import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.UserType;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
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
        StationGraph stationGraph = new StationGraph(sectionRepository.findAll());
        PathFinder pathFinder = new DijkstraShortestPathFinder(stationGraph);
        if(loginMember.getType().equals(UserType.GUEST)){
            return PathResponse.from(pathFinder.findPath(source, target));
        }
        return PathResponse.from(pathFinder.findPathByLoginMember(source, target, loginMember.getAge()));
    }
}
