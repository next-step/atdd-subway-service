package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }


    public PathResponse findShortestPath(Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Section> sections = findAllSections();

        PathFinder pathFinder = DijkstraPathFinder.from(sections);
        List<Station> stations = pathFinder.findAllStationsByStations(sourceStation, targetStation);
        int distance = pathFinder.findShortestDistance(sourceStation, targetStation);

        return PathResponse.of(stations, distance);
    }

    private List<Section> findAllSections() {
        return lineRepository.findAll()
                .stream()
                .flatMap(l -> l.getSections().stream())
                .collect(Collectors.toList());
    }
}
