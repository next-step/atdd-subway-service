package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public PathService(SectionRepository sectionRepository, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(long sourceId, long targetId) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        StationGraph graph = new StationGraph(sectionRepository.findAll());
        Path path = new PathFinder(graph).find(source, target);
        return PathResponse.of(path);
    }

}
