package nextstep.subway.path.application;

import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Sections sections = sectionService.findAll();
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);

        PathFinder pathFinder = PathFinder.of(sections);
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);

        return PathResponse.toPath(path);
    }
}
