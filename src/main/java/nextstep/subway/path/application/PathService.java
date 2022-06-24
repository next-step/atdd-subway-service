package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final SectionService sectionService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(SectionService sectionService, StationService stationService, PathFinder pathFinder) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        List<Section> allSections = sectionService.findAllSections();
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        Path path = pathFinder.findShortestPath(allSections, source, target);
        return PathResponse.of(path);
    }
}
