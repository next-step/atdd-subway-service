package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<Station> stations, int distance, int fare) {
        this.stations = toStationResponse(stations);
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse(PathFinder pathFinder, int fare) {
        this.stations = toStationResponse(pathFinder.findStations());
        this.distance = pathFinder.findDistance();
        this.fare = fare;
    }

    private static List<StationResponse> toStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() {
        return fare;
    }
}
