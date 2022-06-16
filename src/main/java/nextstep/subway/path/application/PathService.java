package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationRepository stationRepository;
    private final LineService lineService;
    private final StationService stationService;


    public PathService(StationRepository stationRepository, LineService lineService, StationService stationService) {
        this.stationRepository = stationRepository;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestDistancePath(Long sourceId, Long targetId) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        List<Station> stations = stationRepository.findAll();
        Sections sections = lineService.findAllSections();
        List<Station> shortestStations = PathFinder.dijkstra(source, target, stations, sections);
        Sections filteredSections = sections.filteredBy(shortestStations);

        return PathResponse.of(shortestStations, filteredSections.totalDistance());
    }
}
