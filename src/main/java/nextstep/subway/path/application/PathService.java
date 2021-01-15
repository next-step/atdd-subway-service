package nextstep.subway.path.application;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(final StationService stationService, final PathFinder pathFinder) {
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findDijkstraPath(Long source, Long target) {
        Station startStation = stationService.findById(source);
        Station endStation = stationService.findById(target);

        DijkstraShortestPath shortestPath = pathFinder.findDijkstraPath();

        List<Station> pathStations = pathFinder.findShortestPathStations(shortestPath, startStation, endStation);
        int distance = pathFinder.findShortestPathDistance(shortestPath, startStation, endStation);

        return getPathResponse(pathStations, distance);
    }

    private PathResponse getPathResponse(List<Station> stations, int distance) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance);
    }
}
