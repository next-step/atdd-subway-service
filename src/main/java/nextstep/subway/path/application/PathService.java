package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public PathService(StationService stationService, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPath(PathRequest request) {
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        List<Section> allSections = sectionRepository.findAll();
        PathFinder pathFinder = PathFinder.of(allSections);

        Path path = pathFinder.findShortestPath(source, target);

        return PathResponse.from(path.getStations(), path.getDistance());
    }

}
