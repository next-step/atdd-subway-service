package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.strategy.Dijkstra;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestDistancePath(Integer age, Long sourceId, Long targetId) {
        Station source = stationService.findStationByIdOrElseThrow(sourceId);
        Station target = stationService.findStationByIdOrElseThrow(targetId);
        Sections sections = lineService.findAllSections();

        List<Station> shortestStations = PathFinder.find(new Dijkstra(source, target, sections));
        Sections filteredSections = sections.filteredBy(shortestStations);
        Fare fare = filteredSections.fare(age);

        return PathResponse.of(shortestStations, filteredSections.totalDistance(), fare);
    }
}
