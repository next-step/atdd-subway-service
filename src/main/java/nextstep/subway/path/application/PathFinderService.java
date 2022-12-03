package nextstep.subway.path.application;


import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.DijkstraShortestPathStrategy;
import nextstep.subway.path.domain.PathStrategy;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
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

    public PathResponse getShortestPath(Long sourceId, Long targetId) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        PathStrategy pathStrategy = new DijkstraShortestPathStrategy();
        StationGraph stationGraph = new StationGraph(pathStrategy, sectionRepository.findAll());
        PathFinder pathFinder = PathFinder.of(stationGraph);
        Path path = pathFinder.findPathFromGraph(source, target);
        return PathResponse.from(path);
    }
}
