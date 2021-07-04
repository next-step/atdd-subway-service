package nextstep.subway.path.service;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.service.SectionService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private SectionService sectionService;
    private StationService stationService;
    private PathFinder pathFinder;

    public PathResponse findPaths(PathRequest request) {
        Sections sections = sectionService.findSections();
        Station sourceStation = stationService.findById(request.getSourceStationId());
        Station targetStation = stationService.findById(request.getTargetStationId());
        Path path = pathFinder.findShortestPath(sections, sourceStation, targetStation);
        return PathResponse.of(path);
    }
}
