package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final LineRepository lineRepository;
    private PathFinder pathFinder;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        pathFinder = new PathFinder();
    }

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Section> allSection = lineRepository.findAll().stream()
            .map(line -> line.getSections())
            .flatMap(sections -> sections.stream())
            .collect(Collectors.toList());
        return pathFinder.findShortestPath(allSection, sourceStation, targetStation);
    }
}
