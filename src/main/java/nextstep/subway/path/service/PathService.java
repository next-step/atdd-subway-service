package nextstep.subway.path.service;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.component.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.repository.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(SectionRepository sectionRepository,
        StationService stationService,
        PathFinder pathFinder) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        Sections sections = Sections.from(sectionRepository.findAll());
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());

        return pathFinder.findPath(sections, source, target);
    }
}
