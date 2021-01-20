package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.fare.FareRule.findFareByDistance;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station startStation = stationService.findById(source);
        Station endStation = stationService.findById(target);

        PathFinder pathFinder = getPathFinder();

        List<Station> pathStations = pathFinder.findShortestPathStations(startStation, endStation);
        int distance = pathFinder.findShortestPathDistance(startStation, endStation);
        long fare = getFare(pathStations, distance);

        return getPathResponse(pathStations, distance, fare);
    }

    private long getFare(List<Station> pathStations, int distance) {
        long lineMaxFare = 0;

        for (int i = 1; i< pathStations.size(); i++) {
            Station upStation = pathStations.get(i-1);
            Station downStation = pathStations.get(i);
            Section section = lineService.findSectionByStation(upStation, downStation);

            long lineFare = section.getLine().getFare();
            lineMaxFare = lineFare > lineMaxFare ? lineFare : lineMaxFare;
        }

        return lineMaxFare + findFareByDistance(distance);
    }

    private PathResponse getPathResponse(List<Station> stations, int distance, long fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance, fare);
    }

    private PathFinder getPathFinder() {
        List<Station> stations = stationService.findAll();
        List<Section> sections = lineService.findAllSection();

        return new PathFinder(stations, sections);
    }
}
