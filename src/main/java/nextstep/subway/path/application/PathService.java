package nextstep.subway.path.application;

import nextstep.subway.line.domain.*;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public PathService(final SectionRepository sectionRepository, final StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(final Long source, final Long target) {
        final Station sourceStation = stationService.findStationById(source);
        final Station targetStation = stationService.findStationById(target);
        final List<Section> sections = sectionRepository.findAll();
        final PathFinder pathFinder = PathFinder.of(sourceStation, targetStation, sections);
        return pathFinder.findShortestPath();
    }
}
