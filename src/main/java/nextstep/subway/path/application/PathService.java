package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findDijkstraPath(Long source, Long target) {
        Station startStation = stationService.findById(source);
        Station endStation = stationService.findById(target);

        PathFinder pathFinder = getPathFinder();

        List<Station> pathStations = pathFinder.findShortestPathStations(startStation, endStation);
        int distance = pathFinder.findShortestPathDistance(startStation, endStation);

        return getPathResponse(pathStations, distance);
    }

    private PathResponse getPathResponse(List<Station> stations, int distance) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance);
    }

    private PathFinder getPathFinder() {
        List<Station> stations = stationService.findAll();
        List<Section> sections = lineService.findAllSection();

        return new PathFinder(stations, sections);
    }
}
